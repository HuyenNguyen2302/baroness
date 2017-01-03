package hbnguyen;

import ks.common.games.Solitaire;
import ks.common.model.*;

/**
 * Move card from top of deck to 5 piles
 * @author huyennguyen
 *
 */
public class DealCardMove extends Move {
	protected Deck deck;
	protected Pile[] piles = new Pile[Baroness.PILE_NUM];
	protected int numToDeal;
	
	public DealCardMove(Deck deck, Pile[] piles) {
		this.deck = deck;
		this.piles = piles;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)) { return false; }
		numToDeal = (((Baroness) game).deck.count() >= Baroness.PILE_NUM) ? Baroness.PILE_NUM : ((Baroness) game).deck.count(); 
		for (int pileNum = 0; pileNum < numToDeal; pileNum++) {
			Card card = deck.get();
			piles[pileNum].add(card);
		}
		
		game.updateNumberCardsLeft(-numToDeal);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		for (int pileNum = 0; pileNum < numToDeal; pileNum++) {			
			Card card = piles[Baroness.PILE_NUM - pileNum - 1].get();
			deck.add(card);
		}
		game.updateNumberCardsLeft(+numToDeal);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		return !deck.empty();
	}
}
