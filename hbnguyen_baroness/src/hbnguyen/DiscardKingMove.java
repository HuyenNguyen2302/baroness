package hbnguyen;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

public class DiscardKingMove extends Move {
	protected Pile pile;
	protected Card kingCard;
	protected Pile discardPile;
	protected Baroness baroness;

	public DiscardKingMove(Pile pile, Card kingCard, Pile discardPile) {
		this.pile = pile;
		this.kingCard = kingCard;
		this.discardPile = discardPile;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game))
			return false;
		// pile.get();
		discardPile.add(kingCard);
		game.updateScore(+1);
		return true; 
	}

	@Override
	public boolean undo(Solitaire game) {
		Card kingCard = discardPile.get();
		pile.add(kingCard);
		game.updateScore(-1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {	
		return (kingCard.getRank() == 13);
	}	
}
