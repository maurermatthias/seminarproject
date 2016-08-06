package dbentities;

public enum Visibility {
	ALL, NOTALL;

    public static Visibility fromInteger(int x) {
        switch(x) {
        case 0:
            return ALL;
        case 1:
            return NOTALL;
        }
        return null;
    }
    
    public static int toInteger(Visibility visibility){
        switch(visibility) {
        case ALL:
            return 0;
        case NOTALL:
            return 1;
        }
        return 1;
    }
}
