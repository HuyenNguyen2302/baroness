package hbnguyen;

import java.awt.Dimension;

import heineman.klondike.BuildablePileController;
import heineman.klondike.FoundationController;
import heineman.klondike.KlondikeDeckController;
import heineman.klondike.WastePileController;
import ks.client.gamefactory.GameWindow;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.*;
import ks.common.view.*;
import ks.launcher.Main;


public class Baroness extends Solitaire{
	public static final int PILE_NUM = 5;
	
	/** Each Game has a Deck. */
	protected Deck deck;
	
	/** And 5 piles. */
	protected Pile[] piles = new Pile[PILE_NUM]; 
	
	/** And a discard pile. */
	protected Pile discardPile;
	
	/** The view of the deck */
	protected DeckView deckView;
	
	/** The display for the piles. */
	protected PileView[] pileViews = new PileView[PILE_NUM];
	
	/** The display for the discardPile. */
	protected PileView discardPileView;
	
	/** The display for the score. */
	protected IntegerView scoreView;
	
	/** View for the number of cards left in the deck. */
	protected IntegerView numLeftView;
	
	/**
	 * Baroness constructor comment.
	 */
	public Baroness() {
		super();
	}

	@Override
	public String getName() {
		return "hbnguyen - Baroness";
	}

	@Override
	public boolean hasWon() {
		return getScore().getValue() == 52;
	}

	@Override
	public void initialize() { 
		
		// initialize model
		initializeModel(getSeed());
		initializeView();
		initializeControllers();
		
		// prepare game by dealing facedown cards to all columns, then one face up
		
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1280, 1280);	
	}
	
	/**
	 * initialize method comment.
	 */
	protected void initializeModel (int seed) {
		deck = new Deck("d");
		deck.create(seed);
		model.addElement (deck);   // add to our model (as defined within our superclass).

		// each of the piles appears here
		for (int i = 0; i < PILE_NUM; i++) {
			piles[i] = new Pile ("pile" + i);
			model.addElement (piles[i]);
		} 

		// develop discard pile
		discardPile = new Pile ("discardPile");
		model.addElement (discardPile);

		// initial score is set to ZERO (every Solitaire game by default has a score) and there are 52 cards left.
		// NOTE: These will be added to the model by solitaire Base Class.
		this.updateNumberCardsLeft(52);

		// Lastly, as part of the mode, we will eventually provide a way to register the 
		// type of allowed moves. This feature will be necessary for SolitaireSolvers
	}
	
	protected void initializeView() {
		CardImages ci = getCardImages();

		deckView = new DeckView (deck);
		deckView.setBounds (20, 20, ci.getWidth(), ci.getHeight());
		container.addWidget (deckView);

		// create BuildablePileViews, one after the other (default to 13 full cards -- more than we'll need)
		for (int pileNum = 0; pileNum < PILE_NUM; pileNum++) {
			pileViews[pileNum] = new PileView (piles[pileNum]);
			pileViews[pileNum].setBounds (20*(pileNum+2) + ci.getWidth()*(pileNum+1), 20, ci.getWidth(), ci.getHeight());
			container.addWidget (pileViews[pileNum]);
		}

		// create discardPileView

		discardPileView = new PileView (discardPile);
		discardPileView.setBounds (140 + ci.getWidth()*6, 20, ci.getWidth(), ci.getHeight());
		container.addWidget (discardPileView);

		scoreView = new IntegerView (getScore());
		scoreView.setFontSize(14);
		scoreView.setBounds (160+7*ci.getWidth(), 20, 80, 60);
		container.addWidget (scoreView);

		numLeftView = new IntegerView (getNumLeft());
		numLeftView.setFontSize (14);
		numLeftView.setBounds (180 + 8*ci.getWidth(), 20, 80, 60);
		container.addWidget (numLeftView);
	}
	
	/**
	 * initialize controllers comment.
	 */
	protected void initializeControllers() {
		// Initialize Controllers for DeckView
		deckView.setMouseAdapter(new DeckController (this, deck, piles));
		deckView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		deckView.setUndoAdapter (new SolitaireUndoAdapter(this));
		
		// Now for each Pile
		for (int i = 0; i < PILE_NUM; i++) {
			pileViews[i].setMouseAdapter (new PileController (this, pileViews[i]));
			pileViews[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			pileViews[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}
		
		// Initialize Controllers for discardPileView
		discardPileView.setMouseAdapter(new DiscardPileController (this, discardPileView));
		discardPileView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		discardPileView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// Ensure that any releases (and movement) are handled by the non-interactive widgets
		numLeftView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		numLeftView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		numLeftView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// same for scoreView
		scoreView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		scoreView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		scoreView.setUndoAdapter (new SolitaireUndoAdapter(this));
	
		// Finally, cover the Container for any events not handled by a widget:
		getContainer().setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		getContainer().setMouseAdapter (new SolitaireReleasedAdapter(this));
		getContainer().setUndoAdapter (new SolitaireUndoAdapter(this));
		
	}
	
	/** Count the total number of cards on the board */
	public boolean hasAtLeastFiveCards() {
		int cardNum = 0;
		for (int i = 0; i < PILE_NUM; i++)
			cardNum += piles[i].count();
		return cardNum+1 >= PILE_NUM;
	}
	

	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		// Here the seed is to "order by suit."
		Main.generateWindow(new Baroness(), Deck.OrderBySuit);
		//gw.setSkin(SkinCatalog.MULTIPLE_BOUNCING_BALLS);
	}
}
