package org.hasan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.enums.ResourceType;
import org.gatlin.soa.resource.bean.model.ResourceInfo;
import org.gatlin.soa.user.api.UserService;
import org.gatlin.soa.user.bean.entity.Username;
import org.gatlin.soa.user.bean.enums.UsernameType;
import org.gatlin.util.lang.CollectionUtil;
import org.hasan.bean.HasanCode;
import org.hasan.bean.HasanConsts;
import org.hasan.bean.entity.CfgCookbook;
import org.hasan.bean.entity.CfgGoods;
import org.hasan.bean.entity.CfgGoodsPrice;
import org.hasan.bean.entity.CfgMember;
import org.hasan.bean.entity.UserEvaluation;
import org.hasan.bean.enums.HasanResourceType;
import org.hasan.bean.model.EvaluationInfo;
import org.hasan.bean.model.GoodsDetail;
import org.hasan.bean.model.GoodsInfo;
import org.hasan.bean.model.GoodsPriceInfo;
import org.hasan.bean.param.GoodsAddParam;
import org.hasan.bean.param.GoodsModifyParam;
import org.hasan.manager.CookbookManager;
import org.hasan.manager.GoodsManager;
import org.hasan.manager.HasanManager;
import org.hasan.mybatis.dao.CfgGoodsDao;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

@Service
public class GoodsService {
	
	@Resource
	private UserService userService;
	@Resource
	private CfgGoodsDao cfgGoodsDao;
	@Resource
	private HasanManager hasanManager;
	@Resource
	private GoodsManager goodsManager;
	@Resource
	private ConfigService configService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private CookbookManager cookbookManager;
	
	public GoodsDetail goodsDetail(int id) {
		CfgGoods goods = cfgGoodsDao.getByKey(id);
		Assert.notNull(HasanCode.GOODS_NOT_EXIST, goods);
		GoodsDetail detail = new GoodsDetail(goods);
		// 获取商品本身资源
		Query query = new Query().eq("owner", id).in("cfg_id", HasanResourceType.goodsResourceTypes());
		List<ResourceInfo> resources = resourceService.resources(query).getList();
		// 获取菜谱资源
		query = new Query().eq("owner", goods.getCookbookId()).eq("cfg_id", HasanResourceType.COOKBOOK_ICON.mark());
		ResourceInfo cookbook = resourceService.resource(query);
		resources.add(cookbook);
		Map<HasanResourceType, List<ResourceInfo>> map = new HashMap<HasanResourceType, List<ResourceInfo>>();
		if (!CollectionUtil.isEmpty(resources)) {
			for (ResourceInfo resource : resources) {
				HasanResourceType type = HasanResourceType.match(resource.getCfgId());
				List<ResourceInfo> list = map.get(type);
				if (null == list) {
					list = new ArrayList<ResourceInfo>();
					map.put(type, list);
				}
				list.add(resource);
			}
		} 
		detail.setResources(map);
		
		// 设置价格
		List<CfgGoodsPrice> prices = goodsManager.goodsPrices(goods.getId());
		Map<Integer, CfgMember> members = hasanManager.members();
		List<GoodsPriceInfo> priceInfos = new ArrayList<GoodsPriceInfo>();
		prices.forEach(item -> priceInfos.add(new GoodsPriceInfo(item, members.get(item.getMemberId()))));
		detail.setPrices(priceInfos);
		
		// 设置评价
		int num = configService.config(HasanConsts.DEFAULT_EVALUATION_NUM);
		query = new Query().eq("goods_id", goods.getId()).orderByDesc("created").limit(num);
		detail.setEvaluations(evaluations(query).getList());
		return detail;
	}
	
	public Pager<GoodsInfo> goods(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		List<CfgGoods> goods = goodsManager.goods(query);
		if(goods.isEmpty())
			return Pager.empty();
		Set<Integer> ids = new HashSet<Integer>();
		goods.forEach(item -> ids.add(item.getId()));
		query = new Query().in("owner", ids).eq("cfg_id", HasanResourceType.GOODS_ICON.mark());
		List<ResourceInfo> resources = resourceService.resources(query).getList();
		return Pager.<GoodsInfo, CfgGoods>convert(goods, () -> {
			List<GoodsInfo> infos = new ArrayList<GoodsInfo>();
			for (CfgGoods cfgGoods : goods) {
				ResourceInfo icon = null;
				if (!CollectionUtil.isEmpty(resources)) {
					Iterator<ResourceInfo> iterator = resources.iterator();
					while (iterator.hasNext()) {
						ResourceInfo info = iterator.next();
						if (info.getOwner() == cfgGoods.getId()) {
							icon = info;
							iterator.remove();
							break;
						}
					}
				}
				infos.add(new GoodsInfo(cfgGoods, icon));
			}
			return infos;
		});
	}
	
	public Pager<EvaluationInfo> evaluations(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		List<UserEvaluation> evaluations = goodsManager.evaluations(query);
		Set<Long> set = new HashSet<>();
		evaluations.forEach(item -> set.add(item.getUid()));
		query = new Query().in("uid", set).eq("type", UsernameType.MOBILE.mark());
		List<Username> usernames = userService.usernames(query).getList();
		query = new Query().in("owner", set).eq("cfg_id", ResourceType.AVATAR.mark());
		List<ResourceInfo> avatars = resourceService.resources(query).getList();
		List<EvaluationInfo> list = new ArrayList<EvaluationInfo>();
		evaluations.forEach(item -> {
			Username username = null;
			if (!CollectionUtil.isEmpty(usernames)) {
				for (Username temp : usernames) {
					if (temp.getUid() != item.getUid())
						continue;
					username = temp;
					break;
				}
			}
			ResourceInfo avatar = null;
			if (!CollectionUtil.isEmpty(avatars)) {
				for (ResourceInfo temp : avatars) {
					if (temp.getOwner() != item.getUid())
						continue;
					avatar = temp;
					break;
				}
			}
			list.add(new EvaluationInfo(item, avatar, username));
		});
		return new Pager<EvaluationInfo>(list);
	}
	
	public int add(GoodsAddParam param) { 
		CfgCookbook cookbook = cookbookManager.cookbook(param.getCookbookId());
		Assert.notNull(HasanCode.COOKBOOK_NOT_EXIST, cookbook);
		return goodsManager.goodsAdd(param);
	}
	
	public void modify(GoodsModifyParam param) {
		goodsManager.goodsModify(param);
	}
}
