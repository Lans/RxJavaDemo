package com.example.leishun.rxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MainActivity mContext;
    private List<Integer> integerList;
    private Subscription daqiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        final TextView text = (TextView) this.findViewById(R.id.text);

        //simpleDemo();
        //simpleDemo2();
        //simpleDemo22();

        //operatorsMapDemo();
        //operatorsMapStringToInteger();
        // operatorsFlatMap();
        //operatorsFlatMap2();
        //operatorsFlatMap3();
        //operatorsFlatMap4();

        daqiao = Observable.just("daqiao").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                text.setText(s);
            }
        });

    }

    public void goRxAndroid(View view) {
        startActivity(new Intent(mContext, SchedulerActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        daqiao.unsubscribe();
        Log.e(TAG, "Unsubscribe=" + daqiao.isUnsubscribed());
    }

    //filter:  输出和输入相同的元素，并且会过滤掉那些不满足检查条件的。
    //take:    输出最多指定数量
    //doOnNext 允许我们在每次输出一个元素之前做一些额外的事情
    private void operatorsFlatMap4() {
        query("integer").flatMap(new Func1<List<Integer>, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(List<Integer> integers) {
                return Observable.from(integers);
            }
        }).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                return getTitle(integer);
            }
        }).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return !s.equals("01");
            }
        }).take(5).doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, "缓存：" + s);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, s);
            }
        });
    }

    private void operatorsFlatMap3() {
        query("integer").flatMap(new Func1<List<Integer>, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(List<Integer> integers) {
                return Observable.from(integers);
            }
        }).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                return getTitle(integer);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, s);
            }
        });
    }

    //操作符 FlatMap 的使用。遍历List
    private void operatorsFlatMap2() {
        query("integer").flatMap(new Func1<List<Integer>, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(List<Integer> integers) {
                return Observable.from(integers);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG, integer.toString());
            }
        });
    }

    //操作符 FlatMap 没使用之前
    //Observable.from()方法，它接收一个集合作为输入，然后每次输出一个元素给subscriber：
    private void operatorsFlatMap() {
        query("integer").subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                Observable.from(integers).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG, integer.toString());
                    }
                });
            }
        });
    }

    //配合 FlatMap 的方法
    private Observable<List<Integer>> query(String text) {
        integerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            integerList.add(i);
        }
        Observable<List<Integer>> observable = Observable.just(integerList);
        return observable;
    }

    //配合 FlatMap 的方法,根据string 的值，返回另一个值
    private Observable<String> getTitle(int integer) {
        for (int i = 0; i < integerList.size(); i++) {
            if (integer == integerList.get(i)) {
                return Observable.just(integer + "1");
            }
        }
        return null;
    }

    //将String转化成Integer(Subscriber做的事情越少越好)
    //map：数据格式的转化 如：integer----》string
    private void operatorsMapStringToInteger() {
        Observable.just("Hello world").map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return s.hashCode();
            }
        }).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return integer.toString();
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, s);
            }
        });
    }

    //操作符Map的简单使用
    private void operatorsMapDemo() {
        Observable.just("Hello", "World").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s + "map";
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, s);
            }
        });
    }

    //just的简化版
    private void simpleDemo22() {
        Observable.just("Hello", "World!").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, s);
            }
        });
    }

    //just的使用
    //Observable.just就是用来创建只发出一个事件就结束的Observable对象
    private void simpleDemo2() {
        Observable<String> observable = Observable.just("Hello", "World");
        Action1<String> onNextAction1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, s);
            }
        };
        //subscribe方法有一个重载版本，接受三个Action1类型的参数，分别对应OnNext，OnComplete， OnError函数
        observable.subscribe(onNextAction1);
    }

    //print Hello world
    private void simpleDemo() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("World");
                subscriber.onNext("=_=!");
                subscriber.onCompleted();
            }
        });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, s);
            }
        };

        observable.subscribe(subscriber);
    }
}
