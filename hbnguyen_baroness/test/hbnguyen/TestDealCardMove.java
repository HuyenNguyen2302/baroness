package hbnguyen;

import java.awt.event.MouseEvent;

import hbnguyen.Baroness;
import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.*;
import ks.common.view.*;
import ks.launcher.Main;
import ks.tests.KSTestCase;
import ks.tests.model.ModelFactory;

public class TestDealCardMove extends KSTestCase {
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
		ModelFactory.init(baroness.deck, "AH 2H 3H 4H 5H 6H");		
		assertEquals ("6H", baroness.deck.peek().toString());		
		// press a bit offset into the widget.
		MouseEvent press = createPressed(baroness, baroness.deckView, 0, 0);
		baroness.deckView.getMouseManager().handleMouseEvent(press);		
		assertEquals ("6H", baroness.piles[0].peek().toString());
		assertEquals ("5H", baroness.piles[1].peek().toString());
		assertEquals ("4H", baroness.piles[2].peek().toString());
		assertEquals ("3H", baroness.piles[3].peek().toString());
		assertEquals ("2H", baroness.piles[4].peek().toString());
		assertEquals ("AH", baroness.deck.peek().toString());
		assertEquals(1, baroness.deck.count());	
	}
	
	public void testInvalidMove() {
		// case: deal move is invalid because of empty deck
		ModelFactory.init(baroness.deck, "");
		MouseEvent pressInvalid = createPressed(baroness, baroness.deckView, 0, 0);
		baroness.deckView.getMouseManager().handleMouseEvent(pressInvalid);
		DealCardMove dcmInvalid = new DealCardMove(baroness.deck, baroness.piles);
		assertTrue(!dcmInvalid.doMove(baroness));
	}
}
