package hbnguyen;

import java.awt.event.MouseEvent;

import hbnguyen.Baroness;
import hbnguyen.DiscardTwoCardsMove;
import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.*;
import ks.common.view.*;
import ks.launcher.Main;
import ks.tests.KSTestCase;
import ks.tests.model.ModelFactory;

public class TestDiscardTwoCardsMove extends KSTestCase {
	protected Baroness baroness;
	protected GameWindow gw;
	
	@Override
	protected void setUp() {
		baroness = new Baroness();
		gw = Main.generateWindow(baroness, Deck.OrderBySuit);
		ModelFactory.init(baroness.piles[0], "8H");
		ModelFactory.init(baroness.piles[1], "5H");
		ModelFactory.init(baroness.piles[2], "10H");
	}
	
	@Override
	protected void tearDown() {
		gw.dispose();
	}
	
	public void testValidMove() {
		DiscardTwoCardsMove fm = new DiscardTwoCardsMove(
				baroness.piles[0], 
				baroness.piles[1], 
				baroness.piles[0].peek(),
				baroness.piles[1].peek(),
				baroness.discardPile);
		assertTrue (fm.valid(baroness));
		
		// first create a mouse event
		MouseEvent pr = createPressed (baroness, baroness.pileViews[0], 0, 0);
		baroness.pileViews[0].getMouseManager().handleMouseEvent(pr);

		// drop on the first column
		MouseEvent rel = createReleased (baroness, baroness.pileViews[1], 0, 0);
		baroness.pileViews[1].getMouseManager().handleMouseEvent(rel);
		
		assertEquals (0, baroness.piles[0].count());				
		assertEquals(0, baroness.piles[1].count());
		assertEquals (2, baroness.discardPile.count());
		
		// test undo
		fm.undo(baroness);	
		assertEquals (1, baroness.piles[0].count());		
		assertEquals(1, baroness.piles[1].count());
		assertEquals (0, baroness.discardPile.count());
		
	}
	
	public void testInvalidMove() {
		DiscardTwoCardsMove fm = new DiscardTwoCardsMove(baroness.piles[0], 
				baroness.piles[2], 
				baroness.piles[0].peek(),
				baroness.piles[2].peek(),
				baroness.discardPile);
		assertTrue (!fm.valid(baroness));
	}
} 
