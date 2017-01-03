package hbnguyen;

import java.awt.event.MouseAdapter;

import hbnguyen.DealCardMove;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;

public class DeckController extends SolitaireReleasedAdapter {
	/** The game. */
	protected Baroness theGame;

	/** The WastePile of interest. */
	protected Pile[] piles = new Pile[Baroness.PILE_NUM];

	/** The Deck of interest. */
	protected Deck deck;

	/**
	 * KlondikeDeckController constructor comment.
	 */
	public DeckController(Baroness theGame, Deck d, Pile[] piles) {
		super(theGame);
		this.theGame = theGame;
		this.piles = piles;
		this.deck = d;
	}

	/**
	 * Coordinate reaction to the beginning of a Drag Event. In this case,
	 * no drag is ever achieved, and we simply deal upon the pres.
	 */
	public void mousePressed (java.awt.event.MouseEvent me) {

		// Attempting a DealCardMove
		Move m = new DealCardMove (deck, piles);
		if (m.doMove(theGame)) {
			theGame.pushMove (m);     // Successful Move has been made
			theGame.refreshWidgets(); // refresh updated widgets.
		}
	}
}
