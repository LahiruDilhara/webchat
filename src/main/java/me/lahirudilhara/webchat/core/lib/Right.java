package me.lahirudilhara.webchat.core.lib;

public class Right<R> extends Either<Void,R>{
    R data;

    public Right(R data){
        this.isLeft = false;
        this.data = data;
    }

    @Override
    public R getRight() {
        return data;
    }
}
