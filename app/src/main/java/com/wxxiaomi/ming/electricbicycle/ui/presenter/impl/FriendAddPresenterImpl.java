package com.wxxiaomi.ming.electricbicycle.ui.presenter.impl;

import android.os.Bundle;

import com.wxxiaomi.ming.electricbicycle.dao.bean.UserCommonInfo2;
import com.wxxiaomi.ming.electricbicycle.dao.bean.UserLocatInfo;

import com.wxxiaomi.ming.electricbicycle.ui.presenter.base.BasePreImpl;

import com.wxxiaomi.ming.electricbicycle.ui.presenter.FriendAddPresenter;
import com.wxxiaomi.ming.electricbicycle.dao.db.UserService;
import com.wxxiaomi.ming.electricbicycle.common.GlobalManager;
import com.wxxiaomi.ming.electricbicycle.common.rx.SampleProgressObserver;
import com.wxxiaomi.ming.electricbicycle.ui.activity.UserInfoAct;
import com.wxxiaomi.ming.electricbicycle.ui.activity.view.FriendAddView;
import com.wxxiaomi.ming.electricbicycle.ui.weight.adapter.NearFriendRecommendAdapter1;
import com.wxxiaomi.ming.electricbicycle.support.baidumap.LocationUtil;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;


/**
 * Created by 12262 on 2016/6/15.
 */
public class FriendAddPresenterImpl extends BasePreImpl<FriendAddView> implements FriendAddPresenter<FriendAddView> {

    private NearFriendRecommendAdapter1 adapter ;
    private List<UserLocatInfo> tempNearUserList;

    @Override
    public void init() {
        tempNearUserList = new ArrayList<UserLocatInfo>();
        adapter = new NearFriendRecommendAdapter1(mView.getContext(),tempNearUserList);
        mView.setListAdaper(adapter);
        getNearFriend();
    }


    private void getNearFriend() {

        UserService.getInstance().getNearPeople(GlobalManager.getInstance().getUser().id
                , LocationUtil.getInstance().getLatitude()
                , LocationUtil.getInstance().getLongitude())
                .subscribe(new Action1<List<UserLocatInfo>>() {
                    @Override
                    public void call(List<UserLocatInfo> nearByPerson) {
                        if(nearByPerson!=null){
                            tempNearUserList.addAll(nearByPerson);
                            adapter.setLoadingComplete();
                        }

                    }
                });
    }

    @Override
    public void onFindClick(String name) {
        UserService.getInstance().getUserByNameFWeb(name)
                .subscribe(new SampleProgressObserver<List<UserCommonInfo2>>(mView.getContext()) {
                    @Override
                    public void onNext(List<UserCommonInfo2> initUserInfo) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userInfo", initUserInfo.get(0));
                        mView.runActivity(UserInfoAct.class,bundle,false);
                    }
                });
    }
}