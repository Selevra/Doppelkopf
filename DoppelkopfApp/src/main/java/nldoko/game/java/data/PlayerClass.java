package nldoko.game.java.data;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerClass implements Serializable  {

	private static final long serialVersionUID = 3904343739194363784L;
	private String mName;
	private int mID;
	private float mCurrentPoints;
	private float mStartPoints;
	private boolean mIsActive = false;
	private ArrayList<Float> mPointHistroy = new ArrayList<Float>();
	private ArrayList<Float> mPointHistroyATRound = new ArrayList<Float>();
	
	public PlayerClass(int id){
		this.mID 	= id;
		this.mCurrentPoints	= 0;
		this.mStartPoints = 0;
		this.mName = "";
	}
	
	public PlayerClass(int id, String name, float startPoints){
		this.mID 	= id;
		this.mCurrentPoints	= startPoints;
		this.mStartPoints = startPoints;
		this.mName = name;
	}

    public PlayerClass(int id, String name, ArrayList<Float> historyPoints, ArrayList<Float> historyPointsAtRounds){
        this.mID 	= id;
        this.mCurrentPoints	= 0;
        this.mStartPoints = 0;
        this.mName = name;

        setPointHistoy(historyPoints, historyPointsAtRounds);
    }
	

	public boolean isActive(){
		return this.mIsActive;
	}
	
	public void activate(){
		this.mIsActive = true;
	}
	
	public void deactivate(){
		this.mIsActive = false;
	}
	
	public int getID(){
		return this.mID;
	}
	
	public String getName(){
		return this.mName;
	}
		
	public void setName(String n){
		this.mName = n;
	}
	
	public void updatePoints(int pos, Float p){
		if (pos < getPointHistoryAtRoundLength()) {
			// update existing round
			changePointsForRound(pos, p);
		} else {
			// points for a new round
			Float i = mCurrentPoints + p;
			this.mPointHistroy.add(i);
			this.mPointHistroyATRound.add(p);
			this.calculateCurrentPoints();
		}
	}
	
	public float getPoints(){
		return this.mCurrentPoints;
	}
	
	public float getPointHistory(int pos){
		return this.mPointHistroy.get(pos);
	}
	
	public int getPointHistoryLength(){
		return this.mPointHistroy.size();
	}


	public float getPointHistoryPerRound(int pos){
		return this.mPointHistroyATRound.get(pos);
	}
	
	public int getPointHistoryAtRoundLength(){
		return this.mPointHistroyATRound.size();
	}
	
	public void changePointsForRound(int pos, float newPoints) {
		mPointHistroyATRound.set(pos, newPoints);
		if (pos == 0){
			mPointHistroy.set(pos,newPoints);
		} else {
			mPointHistroy.set(pos, mPointHistroy.get(pos-1) + newPoints);
		}
		this.calculateCurrentPoints();
	}
	
	private void calculateCurrentPoints() {
		float sum = 0;
		for (float points : mPointHistroyATRound) {
			sum += points;
		}
		sum += mStartPoints;
		mCurrentPoints = sum;
	}

    public void resetPointHistory() {
        mPointHistroy = new ArrayList<Float>();
        mPointHistroyATRound = new ArrayList<Float>();
        updatePoints(0,(float)0);
    }

    public void resetPointHistoryAndForceStartAndCurrentPointsTo(float p) {
        mStartPoints = p;
        mCurrentPoints = p;
        mPointHistroy = new ArrayList<Float>();
        mPointHistroyATRound = new ArrayList<Float>();
        updatePoints(0,(float)0);
    }

    private void setPointHistoy(ArrayList<Float> points, ArrayList<Float> pointsAtRounds) {
        mPointHistroy = points;
        mPointHistroyATRound = pointsAtRounds;

        if (mPointHistroy != null && mPointHistroy.size() > 0) {
            mCurrentPoints = mPointHistroy.get(mPointHistroy.size() -1);
        }
    }
	
}
