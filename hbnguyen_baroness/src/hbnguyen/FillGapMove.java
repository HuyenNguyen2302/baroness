package hbnguyen;

import ks.common.games.Solitaire;
import ks.common.model.*;

/**
 * Move card from top of deck to 5 piles
 * @author huyennguyen
 *
 */
public class FillGapMove extends Move {
	protected Pile src; 
	protected Card card;
	protected Pile gap;
	
	public FillGapMove(Pile src, Card card, Pile gap) {
		this.src = src;
		this.card = card;
		this.gap = gap;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)) { return false; }
		// src.get();
		gap.add(card);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		Card card = gap.get();
		src.add(card);
		return true; 
	}

	@Override
	public boolean valid(Solitaire game) {
		return (((Baroness) game).hasAtLeastFiveCards()
				&& src.count() >= 1
				&& gap.count() == 0);
	}
}
