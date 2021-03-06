package org.sculptor.betting.core.serviceapi;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sculptor.betting.core.domain.Bet;
import org.sculptor.betting.core.mapper.BettingInstructionMapper;
import org.sculptor.framework.accessimpl.mongodb.DbManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Spring based test with MongoDB.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class BettingServiceTest extends AbstractJUnit4SpringContextTests implements BettingServiceTestBase {

	@Autowired
	private DbManager dbManager;

	@Autowired
	private BettingService bettingService;

	@Before
	public void initTestData() {
	}

	@Before
	public void initDbManagerThreadInstance() throws Exception {
		// to be able to do lazy loading of associations inside test class
		DbManager.setThreadInstance(dbManager);
	}

	@After
	public void dropDatabase() {
		Set<String> names = dbManager.getDB().getCollectionNames();
		for (String each : names) {
			if (!each.startsWith("system")) {
				dbManager.getDB().getCollection(each).drop();
			}
		}
		// dbManager.getDB().dropDatabase();
	}

	private int countRowsInDBCollection(String name) {
		return (int) dbManager.getDBCollection(name).getCount();
	}

	@Test
	public void testPlaceBet() throws Exception {
		Bet bet = new Bet("abc", "1234", 10.0);
		bettingService.placeBet(bet);
		assertEquals(1, countRowsInDBCollection(BettingInstructionMapper.getInstance().getDBCollectionName()));
	}

}
