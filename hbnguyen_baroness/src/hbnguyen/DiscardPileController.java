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

public class DiscardPileController extends java.awt.event.MouseAdapter {
	/** The Baroness Game. */
	protected Baroness theGame;

	/** The specific Foundation pileView being controlled. */
	protected PileView src;
	protected PileView discardPileView;

	protected Pile sourcePile;
	protected Card cardSource;
	protected Pile targetPile;
	protected Card cardTarget;

	public DiscardPileController(Baroness theGame, PileView src) {
		this.theGame = theGame;
		this.src = src;
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
		System.out.println("cardSource = " + cardSource);

		// get the discardPile
		Pile discardPile = theGame.discardPile;
		
		// get the source pile
		sourcePile = (Pile) fromWidget.getModelElement();
		
		Move move = new DiscardKingMove(sourcePile, cardSource, discardPile);
		// else 
			// move = new DiscardTwoCardsMove(sourcePile, targetPile, cardSource, cardTarget, discardPile);
		
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
