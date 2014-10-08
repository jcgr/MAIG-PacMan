package maig;

/**
 * Class used to store and control the maze
 * 
 * @author A.Liapis
 * @modified D.Vitonis
 * @modified A. Hartzen
 */
public class Map{
	
	// --- variables
	protected final int mapsize = 8;
	
    protected final char[] startingmap={ '@',' ',' ',' ',' ',' ',' ',' ',
                                         ' ',' ',' ',' ',' ',' ',' ',' ',
                                         ' ',' ',' ',' ',' ',' ','#',' ',
                                         ' ',' ',' ',' ',' ',' ',' ',' ',
                                         ' ',' ',' ',' ',' ',' ',' ',' ',
                                         ' ',' ',' ',' ',' ',' ',' ',' ',
                                         ' ',' ',' ',' ',' ',' ',' ',' ',
                                         ' ',' ',' ',' ','G',' ',' ',' '
    };
    protected char[] map;

    // --- movement constants
    public static final int UP=0;
    public static final int RIGHT=1;
    public static final int DOWN=2;
    public static final int LEFT=3;

    public char[] getMap(){
        return (char[])map.clone();
    }

    /**
     * Assign reward 1 if won, 0 if lost
     * @param st
     * @return
     */
    public float getReward(char[] st) {
		if (isGoalReached(st))
			return 1;
		if (isAvatarDead(st)){
			return 0;
		}
		return 0;
	}

	public void resetMaze(){
        map = (char[])startingmap.clone();
    }

    /**
     * Returns the map state which results from an initial map state after an
     * action is applied. In case the action is invalid, the returned map is the
     * same as the initial one (no move).
     * @param action taken by the avatar ('@')
     * @param current map before the action is taken
     * @return resulting map after the action is taken
     */
    public char[] getNextState(int action, char[] map){
        char[] nextMap = (char[])map.clone();
        // get location of '@'
        int avatarIndex = getAvatarIndex(map);
        if(avatarIndex==-1){
            return nextMap; // no effect
        }
        int nextAvatarIndex = getNextAvatarIndex(action, avatarIndex);
        if(nextAvatarIndex>=0 && nextAvatarIndex<map.length){
            if(nextMap[nextAvatarIndex]!='#'){
                // change the map
                nextMap[avatarIndex]=' ';
                nextMap[nextAvatarIndex]='@';
            }
        }
        return nextMap;
    }

    public char[] getNextState(int action){
        char[] nextMap = (char[])map.clone();
        // get location of '@'
        int avatarIndex = getAvatarIndex(map);
        if(avatarIndex==-1){
            return nextMap; // no effect
        }
        int nextAvatarIndex = getNextAvatarIndex(action, avatarIndex);
        //System.out.println(avatarIndex+" "+nextAvatarIndex);
        if(nextAvatarIndex>=0 && nextAvatarIndex<map.length){
            if(nextMap[nextAvatarIndex]!='#'){
                // change the map
                nextMap[avatarIndex]=' ';
                nextMap[nextAvatarIndex]='@';
            }
        }
        return nextMap;
    }
    
    public char[] getNextGhostState(int action){
        char[] nextMap = (char[])map.clone();
        // get location of '#'
        int ghostIndex = getGhostIndex(map);
        if(ghostIndex==-1){
            return nextMap; // no effect
        }
        int nextGhostIndex = getNextAvatarIndex(action, ghostIndex);
        //System.out.println(avatarIndex+" "+nextAvatarIndex);
        if(nextGhostIndex>=0 && nextGhostIndex<map.length){
            if(nextMap[nextGhostIndex]!='G' && nextMap[nextGhostIndex]!='@'){
                // change the map
                nextMap[ghostIndex]=' ';
                nextMap[nextGhostIndex]='#';
            }
        }
        return nextMap;
    }
    
    public char[] getNextGhostState(int action,char[] map){
        char[] nextMap = (char[])map.clone();
        // get location of '#'
        int ghostIndex = getGhostIndex(map);
        if(ghostIndex==-1){
            return nextMap; // no effect
        }
        int nextGhostIndex = getNextAvatarIndex(action, ghostIndex);
        //System.out.println(avatarIndex+" "+nextAvatarIndex);
        if(nextGhostIndex>=0 && nextGhostIndex<map.length){
            if(nextMap[nextGhostIndex]!='G' && nextMap[nextGhostIndex]!='@'){
                // change the map
                nextMap[ghostIndex]=' ';
                nextMap[nextGhostIndex]='#';
            }
        }
        return nextMap;
    }

    public void goToNextState(int action){
        map=getNextState(action);
    }
    
    public void goToNextGhostState(int action){
        map=getNextGhostState(action);
    }

    public boolean isValidMove(int action){
    	int avatarIndex = getAvatarIndex(map);
        if(avatarIndex==-1){
            return false; // no effect
        }
        int nextAvatarIndex = getNextAvatarIndex(action, avatarIndex);
        if(nextAvatarIndex>=0 && nextAvatarIndex<map.length && avatarIndex!=nextAvatarIndex){
            if(map[nextAvatarIndex]!='#'){
            	return true;
            }
        }
        return false;
    }

    public boolean isValidMove(int action, char[] map){
    	int avatarIndex = getAvatarIndex(map);
        if(avatarIndex==-1){
            return false; // no effect
        }
        int nextAvatarIndex = getNextAvatarIndex(action, avatarIndex);
        if(nextAvatarIndex>=0 && nextAvatarIndex<map.length && avatarIndex!=nextAvatarIndex){
            if(map[nextAvatarIndex]!='#'){
            	return true;
            }
        }
        return false;
    }
    
    public boolean isValidGhostMove(int action, char[] map){
    	int ghostIndex = getGhostIndex(map);
        if(ghostIndex==-1){
            return false; // no effect
        }
        int nextGhostIndex = getNextAvatarIndex(action, ghostIndex);
        if(nextGhostIndex>=0 && nextGhostIndex<map.length 
        		&& ghostIndex!=nextGhostIndex && nextGhostIndex!=getGoalIndex(map)){
            if(map[nextGhostIndex]!='@'){
            	return true;
            }
        }
        return false;
    }
    
    public boolean isValidGhostMove(int action){
    	int ghostIndex = getGhostIndex(map);
        if(ghostIndex==-1){
            return false; // no effect
        }
        int nextGhostIndex = getNextAvatarIndex(action, ghostIndex);
        if(nextGhostIndex>=0 && nextGhostIndex<map.length 
        		&& ghostIndex!=nextGhostIndex && nextGhostIndex!=getGoalIndex(map)){
            if(map[nextGhostIndex]!='@'){
            	return true;
            }
        }
        return false;
    }

    public int getAvatarIndex(){
        int avatarIndex = -1;
        for(int i=0;i<map.length;i++){
            if(map[i]=='@'){ avatarIndex=i; }
        }
        return avatarIndex;
    }

    public int getAvatarIndex(char[] map){
        int avatarIndex = -1;
        for(int i=0;i<map.length;i++){
            if(map[i]=='@'){ avatarIndex=i; }
        }
        return avatarIndex;
    }
    
    public int getGhostIndex(char[] map){
        int ghostIndex = -1;
        for(int i=0;i<map.length;i++){
            if(map[i]=='#'){ ghostIndex=i; }
        }
        return ghostIndex;
    }
    
    public int getGhostIndex(){
        int ghostIndex = -1;
        for(int i=0;i<map.length;i++){
            if(map[i]=='#'){ ghostIndex=i; }
        }
        return ghostIndex;
    }
    
    public int getGoalIndex(char[] map){
        int goalIndex = -1;
        for(int i=0;i<map.length;i++){
            if(map[i]=='G'){ goalIndex=i; }
        }
        return goalIndex;
    }

    public boolean isGoalReached(){
        int goalIndex = -1;
        for(int i=0;i<map.length;i++){
            if(map[i]=='G'){ goalIndex=i; }
        }
        return (goalIndex==-1);
    }
    
    public boolean isAvatarDead(char[] map){
    	
    	int currentAvatarIndex = getAvatarIndex(map);
    	int currentGhostIndex = getGhostIndex(map);
    	
    	int avatarx = currentAvatarIndex%8;
        int avatary = currentAvatarIndex/8;
        
        int ghostx = currentGhostIndex%8;
        int ghosty = currentGhostIndex/8;
    	
        if (Math.abs(avatarx - ghostx)<2 && Math.abs(avatary - ghosty)<2){
//        System.out.println("dead");
        	return true;
        }
        
        return false;
        
    }

    public boolean isGoalReached(char[] map){
        int goalIndex = -1;
        for(int i=0;i<map.length;i++){
            if(map[i]=='G'){ goalIndex=i; }
        }
        return (goalIndex==-1);
    }

    public int getNextAvatarIndex(int action, int currentAvatarIndex){
        int x = currentAvatarIndex%8;
        int y = currentAvatarIndex/8;
        if(action==UP){
            y--;
        }
        if(action==RIGHT){
            x++;
        } else if(action==DOWN){
            y++;
        } else if(action==LEFT){
            x--;
        }
        if(x<0 || y<0 || x>=8 || y>=8){
            return currentAvatarIndex; // no move
        }
        return x+8*y;
    }

    public void printMap(){
        for(int i=0;i<map.length;i++){
            if(i%8==0){
                System.out.println("+-+-+-+-+-+-+-+-+");
            }
            System.out.print("|"+map[i]);
            if(i%8==7){
                System.out.println("|");
            }
        }
        System.out.println("+-+-+-+-+-+-+-+-+");
    }

    public void printMap(char[] map){
        for(int i=0;i<map.length;i++){
            if(i%8==0){
                System.out.println("+-+-+-+-+-+-+-+-+");
            }
            System.out.print("|"+map[i]);
            if(i%8==7){
                System.out.println("|");
            }
        }
        System.out.println("+-+-+-+-+-+-+-+-+");
    }

    public String getMoveName(int action){
        String result = "ERROR";
        if(action==UP){
            result="UP";
        } else if(action==RIGHT){
            result="RIGHT";
        } else if(action==DOWN){
            result="DOWN";
        } else if(action==LEFT){
            result="LEFT";
        }
        return result;
    }

	public int getGoodGhostAction(char[] map) {
		int currentAvatarIndex = getAvatarIndex(map);
    	int currentGhostIndex = getGhostIndex(map);
    	
    	int avatarx = currentAvatarIndex%8;
        int avatary = currentAvatarIndex/8;
        
        int ghostx = currentGhostIndex%8;
        int ghosty = currentGhostIndex/8;
    	
        int manhatanDistance = Math.abs(ghosty-avatary)+Math.abs(ghostx-avatarx);
        
        int goodAction = 0;
        for (int i=0;i<4;i++){
        	int possition = getNextAvatarIndex(i,currentGhostIndex);
        	ghostx = possition%8;
	        ghosty = possition/8;
	        int newmanhatan = Math.abs(ghosty-avatary)+Math.abs(ghostx-avatarx);
	        if (newmanhatan < manhatanDistance){
	        	goodAction = i;
	        	manhatanDistance = newmanhatan;
	        }
        }
        return goodAction;
	}
	
}
