package me.lahirudilhara.webchat.core.lib;

public abstract class Either<L,R> {
    protected boolean isLeft;
    public boolean isLeft(){
        return isLeft;
    }
    public boolean isRight() {
        return isLeft;
    }
    public L getLeft(){
        return null;
    }
    public R getRight(){
        return null;
    }
}
