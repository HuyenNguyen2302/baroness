package hbnguyen;

import java.awt.event.MouseEvent;

import hbnguyen.Baroness;
import hbnguyen.DiscardKingMove;
import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.*;
import ks.common.view.*;
import ks.launcher.Main;
import ks.tests.KSTestCase;
import ks.tests.model.ModelFactory;

public class TestDiscardKingMove extends KSTestCase {
	protected Baroness baroness;
	protected GameWindow gw;
	
	@Override
	protected void setUp() {
		baroness = new Baroness();
		gw = Main.generateWindow(baroness, Deck.OrderBySuit);
		ModelFactory.init(baroness.piles[0], "KH");
		ModelFactory.init(baroness.piles[1], "3H");
	}
	
	@Override
	protected void tearDown() {
		gw.dispose();
	}
	
	public void testValidMove() {
		// first create a mouse event
		MouseEvent pr = createPressed (baroness, baroness.pileViews[0], 0, 0);
		baroness.pileViews[0].getMouseManager().handleMouseEvent(pr);

		// drop on the first column
		MouseEvent rel = createReleased (baroness, baroness.discardPileView, 0, 0);
		baroness.discardPileView.getMouseManager().handleMouseEvent(rel);
		assertEquals (0, baroness.piles[0].count());
		assertEquals(1, baroness.discardPile.count());
	}
	
	public void testInvalidMove() {
		Card drag = baroness.piles[1].get();
		DiscardKingMove fm = new DiscardKingMove(baroness.piles[1], drag, baroness.discardPile);
		assertTrue (!fm.doMove(baroness));
	}
}
