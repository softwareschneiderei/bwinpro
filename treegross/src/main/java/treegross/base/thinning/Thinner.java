package treegross.base.thinning;

import treegross.base.Stand;


/**
 * Interface to perform thinning on a stand. There are several different algorithms
 * to perform thinning that can be configured by using the respective
 * 
 * @see treegross.base.thinning.ThinningType
 * 
 */
public interface Thinner {
    
    void thin(Stand stand);

}
