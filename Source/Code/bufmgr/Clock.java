package bufmgr;

/**
 * The "Clock" replacement policy.
 */
class Clock extends Replacer {

  //
  // Frame State Constants
  //
  protected static final int AVAILABLE = 10;
  protected static final int REFERENCED = 11;
  protected static final int PINNED = 12;

  /** Clock head; required for the default clock algorithm. */
  protected int head;

  // --------------------------------------------------------------------------

  /**
   * Constructs a clock replacer.
   */
  public Clock(BufMgr bufmgr) {
    super(bufmgr);

    // initialize the frame states, mark all of them as available
    for (int i = 0; i < frametab.length; i++) {
    	//shivani
    	frametab[i].state=AVAILABLE;
    }

    // initialize the clock head
    head = -1;

  } // public Clock(BufMgr bufmgr)

  /**
   * Notifies the replacer of a new page.
   */
  public void newPage(FrameDesc fdesc) {
    // no need to update frame state
  }

  /**
   * Notifies the replacer of a free page.
   */
  public void freePage(FrameDesc fdesc) { 
    fdesc.state = AVAILABLE;
  }

  /**
   * Notifies the replacer of a pined page updating the state.
   */
  public void pinPage(FrameDesc fdesc) {
  //update the state as PINNED
	//shivani
	  fdesc.state=PINNED;
  }

  /**
   * checks if a page in unpinned and notifies the replacer by updating the state.
   */
  public void unpinPage(FrameDesc fdesc) {
  // if the pincnt is 0 -> update the state as REFERENCED
	//shivani
	  if(fdesc.pincnt == 0){
		  fdesc.state=REFERENCED;
		  
	  } 
  }

  /**
   * Selects the best frame to use for pinning a new page.
   * 
   * @return victim frame number, or -1 if none available
   */
  public int pickVictim() {

    // keep track of the number of tries
    int testcnt = 0;
    /**
     * Trying for three times 
     */
    while(testcnt < 3){   	
    	//Shivani Initialize head to first frame for each try
    	head = -1;
	    do {
	
	    	//Shivani advance the clock head
	        head++;
	
	      // stopping state if the clock finds nothing -> return -1
	      if(head >= frametab.length && testcnt > 2){
	      	  //remove Shivani - reset the head to -1 so that it doesn't go out of index next time
	      	  //head = -1;
	      	 return -1; 
	        
	      }
	
	      // make referenced frames available next time
	      //Shivani - 
	      if(head > 0 && (frametab[head-1].state == REFERENCED)){
	      	  frametab[head-1].state = AVAILABLE;  
	//      	  head++;
	        
	      }
	
	    } while (frametab[head].state != AVAILABLE);
	    /**
	     * Returning the head if its state is available. We don't need to retry as we have already find the available frame.
	     */
	    if(frametab[head].state == AVAILABLE){
	    	return head;
	    }
	    testcnt++;	    
  	}   
 // return the victim page
    return head;
  } // public int pick_victim()

} // class Clock extends Replacer
