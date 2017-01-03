package hbnguyen;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import heineman.klondike.MoveCardToFoundationMove;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.BuildablePileView;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class PileController extends java.awt.event.MouseAdapter {
	/** The Baroness Game. */
	protected Baroness theGame;

	/** The specific Foundation pileView being controlled. */
	protected PileView sourcePileView;
	protected PileView discardPileView;

	protected Pile sourcePile;
	protected Card cardSource;
	protected Pile targetPile;
	protected Card cardTarget;

	public PileController(Baroness theGame, PileView sourcePileView) {
		this.theGame = theGame;
		this.sourcePileView = sourcePileView;
		
	}

	/**
	 * Coordinate reaction to the beginning of a Drag Event.
	 *
	 * Note: There is no way to differentiate between a press that will become
	 * part of a double click vs. a click that will be held and dragged. Only
	 * mouseReleased will be able to help us out with that one.
	 *
	 * Creation date: (10/4/01 6:05:55 PM)
	 * 
	 * @param pv
	 *            ks.common.view.PileView
	 * @param me
	 *            java.awt.event.MouseEvent
	 */
	public void mousePressed(MouseEvent me) {
		
		// The container manages several critical pieces of information; namely,
		// it
		// is responsible for the draggingObject; in our case, this would be a
		// CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = theGame.getContainer();

		// Return if there is no card to be chosen. 
		Pile sourcePile = (Pile) sourcePileView.getModelElement();
		if (sourcePile.count() == 0) {
			c.releaseDraggingObject();
			return;
		}

		// Get a card to move from PileView. Note: this returns a CardView.
		// Note that this method will alter the model for BuildablePileView if
		// the condition is met.
		CardView cardSourceView = sourcePileView.getCardViewForTopCard(me);

		cardSource = sourcePile.peek();
		

		// If we get here, then the user has indeed clicked on the top card in
		// the PileView and
		// we are able to now move it on the screen at will. For smooth action,
		// the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println("PileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}

		// Tell container which object is being dragged, and where in that
		// widget the user clicked.
		c.setActiveDraggingObject(cardSourceView, me);

		// Tell container which source widget initiated the drag
		c.setDragSource(sourcePileView);

		// The only widget that could have changed is ourselves. If we called
		// refresh, there
		// would be a flicker, because the dragged widget would not be redrawn.
		// We simply
		// force the WastePile's image to be updated, but nothing is refreshed
		// on the screen.
		// This is patently OK because the card has not yet been dragged away to
		// reveal the
		// card beneath it. A bit tricky and I like it!
		sourcePileView.redraw();
	}

	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * A bit of a challenge to construct the appropriate move, because cards can
	 * be dragged both from the WastePile (as a CardView object) and the
	 * BuildablePileView (as a ColumnView).
	 * 
	 * @param me 
	 * 				java.awt.event.MouseEvent
	 */
	public void mouseReleased(MouseEvent me) {

		Container c = theGame.getContainer();

		// Return if there is no card being dragged chosen.
		Widget draggingWidget = c.getActiveDraggingObject();
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.err.println("PileController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();
			return;
		}

		// Recover the from the source pile
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println("PileController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}
		
		cardSource = (Card) draggingWidget.getModelElement();
		
		

		// Determine the To Pile
		targetPile = (Pile) sourcePileView.getModelElement();

		// get the discardPile
		Pile discardPile = theGame.discardPile;
		Move move = null;
		sourcePile = (Pile) fromWidget.getModelElement();
		
		if (targetPile.count() == 0) {
			move = new FillGapMove(sourcePile, cardSource, targetPile);
		} else {
			// get the top card of the targetPile
			cardTarget = targetPile.peek();
			if (cardTarget == null) {
				System.err.println("PileController::mouseReleased(): somehow no cardTarget in container.");
				c.releaseDraggingObject();
				return;
			}
			move = new DiscardTwoCardsMove(sourcePile, targetPile, cardSource, cardTarget, discardPile);
		}
		 
		if (move.doMove(theGame)) {
			theGame.pushMove (move);     // Successful Move has been Move
		} else {
			fromWidget.returnWidget (draggingWidget);
		}
		 
		// Ahhhh. Instead of dealing with multiple 'instanceof' difficulty, why
		// don't we allow
		// for multiple controllers to be set on the same widget? Each will be
		// invoked, one
		// at a time, until someone returns TRUE (stating that they are
		// processing the event).
		// Then we have controllers for each MOVE TYPE, not just for each
		// entity. In this way,
		// I wouldn't have to convert the CardView from wastePile into a
		// ColumnView. I would
		// still have to do some sort of instanceOf check, however, to validate:
		// But if the
		// instanceof failed, the controller could safely return and say NOT ME!
		// See! There
		// always is a way to avoid layered if statements in OO.

		// release the dragging object, (this will reset dragSource)
		c.releaseDraggingObject();

		// finally repaint
		c.repaint();
		
	}
}
