package local;

import java.util.Comparator;

public class Score implements Comparator<Score>{
	public static final int MAX_SCORE = 0x10000;
	private String player;
	private int score;
	
	
	public String getPlayer(){
		return player;
	}
	
	public void setPlayer(String name){
		player = name;
	}
	
	public int getScore(){
		return score;
	}
	
	public void setScore(int s){
		score = s;
	}
	
	public static Score mapToScore(String data){
		Score score = new Score();		
		
		String [] scoreData = data.split(" ");
		if(scoreData.length > 1){
			try{
				int s = Integer.parseInt(scoreData[1]);
				score.setPlayer(scoreData[0]);
				score.setScore(s);
			}catch(Exception e){
			
			}			
		}
		return score;		
	}

	//TODO this should some how use Comparator.reversed
	@Override
	public int compare(Score o1, Score o2) {		
		return o2.getScore() - o1.getScore();
	}
	
	@Override
	public String toString(){
		return player + " " + score;
	}
}
