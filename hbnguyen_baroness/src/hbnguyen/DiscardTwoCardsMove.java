package hbnguyen;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.*;

public class DiscardTwoCardsMove extends Move {

	
	protected Pile source;
	protected Pile target;
	protected Card cardSource;
	protected Card cardTarget;
	protected Pile discardPile;
	
	
	protected Baroness baroness;

	public DiscardTwoCardsMove(Pile source, Pile target, Card cardSource, Card cardTarget, Pile discardPile) {
		this.source = source;
		this.target = target;
		this.cardSource = cardSource;
		this.cardTarget = cardTarget;
		this.discardPile = discardPile;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game))
			return false;
		// source.get();
		target.get();
		discardPile.add(cardSource);
		discardPile.add(cardTarget);
		game.updateScore(+2);
		return true; 
	}

	@Override
	public boolean undo(Solitaire game) {
		
		Card cardTarget = discardPile.get();
		target.add(cardTarget);
		Card cardSource = discardPile.get();
		source.add(cardSource);
		game.updateScore(-2);
		
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {	
		return (cardSource.getRank() + cardTarget.getRank()) == 13;
	}	
}
