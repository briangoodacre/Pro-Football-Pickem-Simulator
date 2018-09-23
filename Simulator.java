import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Simulator {
	
	private final static int CHANCE = 60;
	private final static int NUM_RUNS = 100_000;
	
	private final static Random rand = new Random(GregorianCalendar.getInstance().getTimeInMillis());
	
	private final static List<Integer> regularWeeks = new LinkedList<Integer>();
	private final static List<Integer> playoffWeeks = new LinkedList<Integer>();
	
	public static void main(String args[]) {
		// set up weeks
		setUpRegularSeasonWeeks();
		setUpPlayoffs();
		
		// set up players
		List<PlayerScore> players = new ArrayList<>();
		PlayerScore bobby = new PlayerScore("bobby");
		PlayerScore harshal = new PlayerScore("harshal");
		PlayerScore maureen = new PlayerScore("maureen");
		PlayerScore rob = new PlayerScore("rob");
		PlayerScore brian = new PlayerScore("brian");
		PlayerScore monal = new PlayerScore("monal");
		players.add(bobby);
		players.add(harshal);
		players.add(maureen);
		players.add(rob);
		players.add(brian);
		players.add(monal);
		
		for (int num = 0; num < NUM_RUNS; num ++){
			for (PlayerScore player: players) {
				player.clearWeeks();
				recordWeeks(player);
				player.simulateWeeks();
			}
			Collections.sort(players);
			players.get(players.size()-1).incrementWeeksWonCounter();
		}
		
		Comparator<PlayerScore> weeksWonComparator = new Comparator<PlayerScore>() {
		    @Override
		    public int compare(PlayerScore c1, PlayerScore c2) {
		        return c2.weeksWonCounter - c1.weeksWonCounter; // use your logic
		    }
		};
		Collections.sort(players, weeksWonComparator);
		// figure out percentages
		final String percentSign = "%";
		System.out.println("Percentage of picking a game correct = " + CHANCE + "%.");
		for (PlayerScore player: players) {
			final double winningPercentage = (double) (player.weeksWonCounter) / ((double)NUM_RUNS);
			System.out.format("%s: %2.1f%s \n", player.getName(), winningPercentage*100, percentSign);
		}
	}
	
	private static void recordWeeks(PlayerScore player) {
		switch (player.name) {
		case "bobby": 
			player.setWeek(1, 7);
			player.setWeek(2, 11);
				return;
		case "harshal":
			player.setWeek(1, 11);
			player.setWeek(2, 6);
				return;
		case "maureen":
			player.setWeek(1, 10);
			player.setWeek(2, 7);
				return;
		case "rob":
			player.setWeek(1, 9);
			player.setWeek(2, 8);
				return;
		case "brian":
			player.setWeek(1, 10);
			player.setWeek(2, 8);
				return;
		case "monal":
			player.setWeek(1, 8);
			player.setWeek(2, 8);
				return;
		default:
			System.out.println("ISSUE WITH PLAYER NAME");
		}
		
	}
	
	private static void setUpRegularSeasonWeeks(){
		regularWeeks.add(17);//1
		regularWeeks.add(17);//2
		regularWeeks.add(17);//3
		regularWeeks.add(16);//4
		regularWeeks.add(16);//5
		regularWeeks.add(16);//6
		regularWeeks.add(15);//7
		regularWeeks.add(15);//8
		regularWeeks.add(14);//9
		regularWeeks.add(15);//10
		regularWeeks.add(14);//11
		regularWeeks.add(16);//12
		regularWeeks.add(17);//13
		regularWeeks.add(17);//14
		regularWeeks.add(17);//15
		regularWeeks.add(17);//16
		regularWeeks.add(17);//17
	}
	
	private static void setUpPlayoffs() {
		playoffWeeks.add(4);
		playoffWeeks.add(4);
		playoffWeeks.add(2);
		playoffWeeks.add(1);
	}
	
	 protected static class PlayerScore implements Comparable<PlayerScore>{
		private final String name;
		private ArrayList<Integer> regularSeasonScores;
		private ArrayList<Integer> playoffScores;
		private int weeksWonCounter;
		
		public PlayerScore(String name) {
			super();
			this.name = name;
			
			// initialize 
			regularSeasonScores = new ArrayList<>();
			playoffScores = new ArrayList<>();
		}
		
		public void clearWeeks() {// initialize 
			regularSeasonScores = new ArrayList<>();
			for (int i = 0; i < 17; i ++) {
				regularSeasonScores.add(-1);
			}
			playoffScores = new ArrayList<>();
			for (int i = 0; i < 4; i ++) {
				playoffScores.add(-1);
			}
			
		}
		
		

		public int getWeeksWonCounter() {
			return weeksWonCounter;
		}

		public void incrementWeeksWonCounter() {
			this.weeksWonCounter++;
		}



		public String getName() {
			return name;
		}

		public void setWeek(int week, int score) {
			if (week > 17) {
				playoffScores.set(week-18, score);
			} else {
				regularSeasonScores.set(week - 1, score);
			}
		}
		
		public void simulateWeeks() {
			simulateRegularWeeks();
			simulatePlayoffWeeks();
		}
		private void simulateRegularWeeks() {
			int week = 0;
			for (int numGamesThisWeek: regularWeeks) {
				int numCorrect = 0;
				if (regularSeasonScores.get(week) == -1) {
					for (int game = 0; game < numGamesThisWeek; game++) {
						if (rand.nextInt(100) < CHANCE) {
							numCorrect++;
						}
					}
					regularSeasonScores.set(week, numCorrect);
				}
				week++;
			}
		}
		private void simulatePlayoffWeeks() {
			int week = 0;
			for (int numGamesThisWeek: playoffWeeks) {
				int numCorrect = 0;
				if (playoffScores.get(week) == -1) {
					for (int game = 0; game < numGamesThisWeek; game++) {
						if (rand.nextInt(100) < CHANCE) {
							numCorrect++;
						}
					}
					playoffScores.set(week, numCorrect);
				}
				week++;
			}
		}
		
		public static int getRegularSeasonPoints(List<Integer> scores) {
			Set<Integer> regularScore = new HashSet<>(scores);
			// remove lowest two weeks
			regularScore.remove(Collections.min(scores));
			regularScore.remove(Collections.min(scores));
			
			int sum = 0;
			for (int week : scores) {
				sum += week;
			}
			return sum;
		}
		public static int getPlayoffPoints(List<Integer> scores) {
			int sum = 0;
			for (int week : scores) {
				sum += week;
			}
			return sum;
		}
		
		@Override
		public int compareTo(PlayerScore other) {
			int myScore = 0;
			myScore += getRegularSeasonPoints(regularSeasonScores);
			myScore += getPlayoffPoints(playoffScores);
			
			int theirScore = 0;
			theirScore += getRegularSeasonPoints(other.regularSeasonScores);
			theirScore += getPlayoffPoints(other.playoffScores);

			// tie breaker
			if (myScore == theirScore) {
				// playoff
				for (int i = playoffScores.size()-1; i >= 0; i--) {
					int diff = this.playoffScores.get(i) - other.playoffScores.get(i);
					if (diff != 0) {
						return diff;
					}
				}
				// regular season
				for (int i = regularSeasonScores.size()-1; i >= 0; i--) {
					int diff = this.regularSeasonScores.get(i) - other.regularSeasonScores.get(i);
					if (diff != 0) {
						return diff;
					}
				}
			} else {
				return myScore - theirScore;
			}
			System.out.println("Got to the end??? " + this.name + " and " + other.name);
			return 0;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((playoffScores == null) ? 0 : playoffScores.hashCode());
			result = prime * result + ((regularSeasonScores == null) ? 0 : regularSeasonScores.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this.compareTo((PlayerScore) obj) == 0) {
				return true;
			} else {
				return false;
			}
		}
	}
}


