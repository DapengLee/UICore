package androidx.ui.core.app;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 应用碎片管理<br/>
 * 主要在Activity中替换、添加碎片。<br/>
 */
public class AppFragmentManager {

    /**
     * 碎片 - 添加
     */
    public final static int ADD = 1;
    /**
     * 碎片 - 替换
     */
    public final static int REPLACE = 2;

    /**
     * 应用页面布局
     */
    private AppLayout appLayout;
    /**
     * 碎片集合
     */
    private List<Fragment> fragments;

    /**
     * 应用碎片管理
     *
     * @param appLayout 应用页面布局类
     */
    public AppFragmentManager(AppLayout appLayout) {
        this.appLayout = appLayout;
        fragments = new ArrayList<>();
    }

    /**
     * 获取容器资源id
     *
     * @return 容器资源id
     */
    public int getContainerViewResId() {
        return appLayout.getContainerViewResId();
    }

    /**
     * 获取碎片管理器
     *
     * @return
     */
    public FragmentManager getFragmentManager() {
        return appLayout.getAppFragmentManager();
    }

    /**
     * 添加碎片
     *
     * @param fragment 碎片
     */
    public void add(Fragment fragment) {
        transaction(ADD, fragment, false);
    }

    /**
     * 添加碎片
     *
     * @param fragment       碎片
     * @param addToBackStack 是否添加到回退栈
     */
    public void add(Fragment fragment, boolean addToBackStack) {
        transaction(ADD, fragment, addToBackStack);
    }

    /**
     * 替换碎片
     *
     * @param fragment 碎片
     */
    public void replace(Fragment fragment) {
        transaction(REPLACE, fragment, false);
    }

    /**
     * 替换碎片
     *
     * @param fragment       碎片
     * @param addToBackStack 是否添加到回退栈
     */
    public void replace(Fragment fragment, boolean addToBackStack) {
        transaction(REPLACE, fragment, addToBackStack);
    }

    /**
     * Fragment事务处理
     *
     * @param type           类型{@link #ADD} OR {@link #REPLACE}
     * @param fragment       碎片
     * @param addToBackStack 是否添加到回退栈
     */
    public void transaction(int type, Fragment fragment, boolean addToBackStack) {
        fragments.add(fragment);
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (type == ADD) {
            if (!fragment.isAdded()) {
                transaction.add(getContainerViewResId(),fragment, tag);
            } else {
                Iterator iterator = fragments.iterator();
                while (iterator.hasNext()) {
                    Fragment next = (Fragment) iterator.next();
                    transaction.hide(next);
                }
                fragment.onResume();
                transaction.show(fragment);
            }
        }
        if (type == REPLACE) {
            transaction.replace(getContainerViewResId(), fragment, tag);
        }
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 弹出栈
     */
    public void popBackStack() {
        if (getFragmentManager() == null) {
            new RuntimeException("popTop failed , activity or fragment is null.").printStackTrace();
            return;
        }
        FragmentManager manager = getFragmentManager();
        manager.popBackStackImmediate();
    }

    /**
     * 弹出栈
     *
     * @param position 剩余位置
     */
    public void popBackStack(int position) {
        if (getFragmentManager() == null) {
            new RuntimeException("pop failed , activity or fragment is null.").printStackTrace();
            return;
        }
        FragmentManager manager = getFragmentManager();
        while (manager.getBackStackEntryCount() > position + 1) {
            manager.popBackStackImmediate();
        }
        getFragmentManager().executePendingTransactions();
    }

}
