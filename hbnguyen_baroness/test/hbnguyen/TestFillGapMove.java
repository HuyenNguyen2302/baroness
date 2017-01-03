package hbnguyen;

import java.awt.event.MouseEvent;

import hbnguyen.Baroness;
import hbnguyen.FillGapMove;
import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.*;
import ks.common.view.*;
import ks.launcher.Main;
import ks.tests.KSTestCase;
import ks.tests.model.ModelFactory;

public class TestFillGapMove extends KSTestCase {
	protected Baroness baroness;
	protected GameWindow gw;
	
	@Override
	protected void setUp() {
		baroness = new Baroness();
		gw = Main.generateWindow(baroness, Deck.OrderBySuit);	
	}
	
	@Override
	protected void tearDown() {
		gw.dispose();
	}
	
	public void testValidMove() {
		ModelFactory.init(baroness.piles[0], "QH 8H");
		ModelFactory.init(baroness.piles[1], "5H");
		ModelFactory.init(baroness.piles[2], "10H");
		ModelFactory.init(baroness.piles[3], "5H");
		ModelFactory.init(baroness.piles[4], "");
		
		FillGapMove fm = new FillGapMove(
				baroness.piles[0], 
				baroness.piles[0].peek(), 
				baroness.piles[4]);
		assertTrue (fm.valid(baroness));
		
		// first create a mouse event
		MouseEvent pr = createPressed (baroness, baroness.pileViews[0], 0, 0);
		baroness.pileViews[0].getMouseManager().handleMouseEvent(pr); 

		// drop on the first column
		MouseEvent rel = createReleased (baroness, baroness.pileViews[4], 0, 0);
		baroness.pileViews[4].getMouseManager().handleMouseEvent(rel);
		// assertEquals ("8H", baroness.piles[4].peek().toString());
		assertEquals (1, baroness.piles[0].count());
		assertEquals(1, baroness.piles[4].count()); 
		
		// test undo
		fm.undo(baroness);
		assertEquals (2, baroness.piles[0].count());
		assertEquals(0, baroness.piles[4].count());
	}
	
	public void testInvalidMove() {
		ModelFactory.init(baroness.piles[0], "QH");
		ModelFactory.init(baroness.piles[1], "5H");
		ModelFactory.init(baroness.piles[2], "10H");
		ModelFactory.init(baroness.piles[3], "4H");
		ModelFactory.init(baroness.piles[4], "");
		FillGapMove fm = new FillGapMove(baroness.piles[0], 
				baroness.piles[0].peek(),
				baroness.piles[4]);
		assertTrue (fm.valid(baroness)); 
		// first create a mouse event
		MouseEvent pr = createPressed (baroness, baroness.pileViews[0], 0, 0);
		baroness.pileViews[0].getMouseManager().handleMouseEvent(pr); 

		// drop on the first column
		MouseEvent rel = createReleased (baroness, baroness.pileViews[4], 0, 0);
		baroness.pileViews[4].getMouseManager().handleMouseEvent(rel);
		// assertEquals ("8H", baroness.piles[4].peek().toString());
		assertEquals (1, baroness.piles[0].count());
		assertEquals(0, baroness.piles[4].count());
	}
} 
