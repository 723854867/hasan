package org.hasan.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.gatlin.core.bean.info.Pager;
import org.gatlin.core.util.Assert;
import org.gatlin.dao.bean.model.Query;
import org.gatlin.soa.bean.model.ResourceInfo;
import org.gatlin.soa.config.api.ConfigService;
import org.gatlin.soa.resource.api.ResourceService;
import org.gatlin.soa.resource.bean.enums.ResourceType;
import org.gatlin.soa.resource.bean.param.ResourcesParam;
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
		ResourcesParam rp = new ResourcesParam();
		rp.addOwner(String.valueOf(id));
		rp.setCfgIds(HasanResourceType.goodsResourceTypes());
		Map<Integer, List<ResourceInfo>> resources = resourceService.cfgIdListMap(rp);
		// 获取菜谱资源
		rp = new ResourcesParam();
		rp.addOwner(String.valueOf(goods.getCookbookId()));
		rp.addCfgId(HasanResourceType.COOKBOOK_ICON.mark());
		resources.putAll(resourceService.cfgIdListMap(rp));
		detail.setResources(resources);
		
		// 设置价格
		List<CfgGoodsPrice> prices = goodsManager.goodsPrices(goods.getId());
		Map<Integer, CfgMember> members = hasanManager.members();
		List<GoodsPriceInfo> priceInfos = new ArrayList<GoodsPriceInfo>();
		prices.forEach(item -> priceInfos.add(new GoodsPriceInfo(item, members.get(item.getMemberId()))));
		detail.setPrices(priceInfos);
		
		// 设置评价
		int num = configService.config(HasanConsts.DEFAULT_EVALUATION_NUM);
		Query query = new Query().eq("goods_id", goods.getId()).orderByDesc("created").limit(num);
		detail.setEvaluations(evaluations(query).getList());
		return detail;
	}
	
	public Pager<GoodsInfo> goods(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		List<CfgGoods> goods = goodsManager.goods(query);
		if(goods.isEmpty())
			return Pager.empty();
		Set<String> ids = new HashSet<String>();
		goods.forEach(item -> ids.add(String.valueOf(item.getId())));
		ResourcesParam rp = new ResourcesParam();
		rp.setOwners(ids);
		rp.addCfgId(HasanResourceType.GOODS_ICON.mark());
		Map<String, ResourceInfo> resources = resourceService.ownerMap(rp);
		return Pager.<GoodsInfo, CfgGoods>convert(goods, () -> {
			List<GoodsInfo> infos = new ArrayList<GoodsInfo>();
			for (CfgGoods cfgGoods : goods) 
				infos.add(new GoodsInfo(cfgGoods, resources.get(String.valueOf(cfgGoods.getId()))));
			return infos;
		});
	}
	
	public Pager<EvaluationInfo> evaluations(Query query) {
		if (null != query.getPage())
			PageHelper.startPage(query.getPage(), query.getPageSize());
		List<UserEvaluation> evaluations = goodsManager.evaluations(query);
		if (CollectionUtil.isEmpty(evaluations))
			return Pager.empty();
		Set<String> set = new HashSet<String>();
		evaluations.forEach(item -> set.add(String.valueOf(item.getUid())));
		query = new Query().in("uid", set).eq("type", UsernameType.MOBILE.mark());
		List<Username> usernames = userService.usernames(query).getList();
		ResourcesParam rp = new ResourcesParam();
		rp.setOwners(set);
		rp.addCfgId(ResourceType.AVATAR.mark());
		Map<String, ResourceInfo> avatars = resourceService.ownerMap(rp);
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
			ResourceInfo avatar = avatars.get(String.valueOf(item.getUid()));
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
