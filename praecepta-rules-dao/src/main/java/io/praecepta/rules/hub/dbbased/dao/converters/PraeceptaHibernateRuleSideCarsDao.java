package io.praecepta.rules.hub.dbbased.dao.converters;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.hibernate.dao.HibernateDao;
import io.praecepta.rules.hub.dbbased.model.PraeceptaRuleSideCarsDbModel;

public class PraeceptaHibernateRuleSideCarsDao extends HibernateDao<Long, PraeceptaRuleSideCarsDbModel> {
	private static final Logger logger = LoggerFactory.getLogger(PraeceptaHibernateRuleSideCarsDao.class);

	public Collection<PraeceptaRuleSideCarsDbModel> sideCarsDBModels;

	@Override
	protected Class<PraeceptaRuleSideCarsDbModel> getEntityClazz() {
		return PraeceptaRuleSideCarsDbModel.class;
	}

	@Override
	public Collection<PraeceptaRuleSideCarsDbModel> fetchAll() {
		logger.debug("Inside Side Cars fetchAll");
		PraeceptaRuleSideCarsDbModel sideCarsDbModels = new PraeceptaRuleSideCarsDbModel();
		sideCarsDbModels.setActive(true);
		sideCarsDBModels = super.findByExample(sideCarsDbModels);
		return sideCarsDBModels;
	}

	public Collection<PraeceptaRuleSideCarsDbModel> fetchRuleSideCars() {
		logger.debug("Inside fetch Rule SideCars");
		if (PraeceptaObjectHelper.isObjectEmpty(sideCarsDBModels)) {
			sideCarsDBModels = this.fetchAll();
		}
		return sideCarsDBModels;
	}
}
