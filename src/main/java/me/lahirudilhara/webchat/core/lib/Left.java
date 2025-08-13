package me.lahirudilhara.webchat.core.lib;

public class Left<L> extends Either<L,Void>{
    private L data;

    public Left(L data){
        this.isLeft = true;
        this.data = data;
    }

    @Override
    public L getLeft() {
        return data;
    }

}
