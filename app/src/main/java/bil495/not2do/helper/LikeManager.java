package bil495.not2do.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import bil495.not2do.model.Not2DoModel;

/**
 * Created by burak on 7/20/2017.
 */

public class LikeManager extends HashMap<Long, Not2DoModel> {
    public static LikeManager LIKES = new LikeManager();
}
