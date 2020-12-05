
public class Archer extends GamePiece {
	
	/**
	 * Constructor for an archer at position @param row, @param column and team @param isBlueTeam
	 * Archer has preset stats.
	 */
	public Archer(boolean isBlueTeam) {
		super(isBlueTeam);
		maxHitPoints = 15;
		hitPoints = maxHitPoints;
		attackDamage = 8;
		attackRange = 4;
		moveSpeed = 2;
		critChance = 0.3;
		pieceType = "Archer";
		description = "The archer uses its bow skills to attack enemies up to 4 spaces away.";
	}
}
