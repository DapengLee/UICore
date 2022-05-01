package androidx.ui.core.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.ui.core.R;
import androidx.ui.core.refrsh.SwipeRefreshLayout;
import androidx.ui.core.widget.Placeholder;

import java.util.ArrayList;
import java.util.List;

/**
 * 刷新 + 加载
 */
public class SwipeRefreshLoading extends FrameLayout {

    private int itemCount;
    private int listItem;
    private SwipeRefreshLayout swipeRefresh;
    private FrameLayout swipeFrame;
    private NestedScrollView scrollView;
    private LinearLayout swipeLinear;
    private SwipeRecyclerView swipeRecycler;
    private RecyclerView recycler;
    private SwipeLoadingLayout swipeLoading;
    private Placeholder placeholder;

    public SwipeRefreshLoading(@NonNull Context context) {
        super(context);
        initAttributeSet(context, null);
    }

    public SwipeRefreshLoading(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context, attrs);
    }

    public SwipeRefreshLoading(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context 上下文
     * @param attrs   属性
     */
    protected void initAttributeSet(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.ui_core_refresh_loading, this, true);
        initView();
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwipeRefreshLoading);
            itemCount = array.getInt(R.styleable.SwipeRefreshLoading_itemCount, 10);
            listItem = array.getResourceId(R.styleable.SwipeRefreshLoading_listItem, R.layout.ui_core_item);
            DefAdapter adapter = new DefAdapter(context);
            recycler.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler.setAdapter(adapter);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < itemCount; i++) {
                list.add("");
            }
            adapter.setItems(list);
            array.recycle();
        }
        swipeLoading.attachNestedScrollView(scrollView);
    }

    /**
     * 初始化
     */
    private void initView() {
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeFrame = findViewById(R.id.swipe_frame);
        scrollView = findViewById(R.id.scroll_view);
        swipeLinear = findViewById(R.id.swipe_linear);
        swipeRecycler = findViewById(R.id.swipe_recycler);
        recycler = findViewById(R.id.recycler);
        swipeLoading = findViewById(R.id.swipe_loading);
        placeholder = findViewById(R.id.placeholder);
    }

    /**
     * @return 刷新下Frame
     */
    public FrameLayout getSwipeFrame() {
        return swipeFrame;
    }

    /**
     * @return Recycler父级
     */
    public LinearLayout getSwipeLinear() {
        return swipeLinear;
    }

    /**
     * @return 刷新View
     */
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefresh;
    }

    /**
     * @return 滑动View
     */
    public NestedScrollView getNestedScrollView() {
        return scrollView;
    }

    /**
     * @return 侧滑RecyclerView
     */
    public SwipeRecyclerView getSwipeRecyclerView() {
        return swipeRecycler;
    }

    /**
     * @return RecyclerView
     */
    public RecyclerView getRecyclerView() {
        return recycler;
    }

    /**
     * @return 滑动加载更多
     */
    public SwipeLoadingLayout getSwipeLoading() {
        return swipeLoading;
    }

    /**
     * @return 占位View
     */
    public Placeholder getPlaceholder() {
        return placeholder;
    }

    /**
     * 设置刷新
     *
     * @param refreshing 是否刷新
     */
    public void setRefreshing(boolean refreshing) {
        swipeRefresh.setRefreshing(refreshing);
    }

    /**
     * 设置刷新颜色
     *
     * @param colors 刷新颜色
     */
    public void setColorSchemeColors(int... colors) {
        swipeRefresh.setColorSchemeColors(colors);
    }

    /**
     * 设置刷新监听
     *
     * @param listener 刷新监听
     */
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefresh.setOnRefreshListener(listener);
    }

    /**
     * 设置加载更多
     *
     * @param loading 是否加载更多
     */
    public void setLoading(boolean loading) {
        swipeLoading.setLoading(loading);
    }

    /**
     * 刷新+加载完毕
     */
    public void finish() {
        setRefreshing(false);
        setLoading(false);
    }

    /**
     * 设置加载更多
     *
     * @param listener 加载更多监听
     */
    public void setOnLoadingListener(SwipeLoadingLayout.OnLoadingListener listener) {
        swipeLoading.setOnLoadingListener(listener);
    }

    /**
     * 设置布局管理器
     *
     * @param manager 布局管理器
     */
    public void setLayoutManager(LinearLayoutManager manager) {
        setLayoutManager(manager, false);
    }

    /**
     * 设置布局管理器
     *
     * @param manager 布局管理器
     * @param swipe   是否侧滑
     */
    public void setLayoutManager(LinearLayoutManager manager, boolean swipe) {
        if (swipe) {
            swipeRecycler.setVisibility(VISIBLE);
            recycler.setVisibility(GONE);
            swipeRecycler.setLayoutManager(manager);
        } else {
            swipeRecycler.setVisibility(GONE);
            recycler.setVisibility(VISIBLE);
            recycler.setLayoutManager(manager);
        }
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public <T extends RecyclerAdapter> void setAdapter(T adapter) {
        adapter.setPlaceholder(placeholder);
        recycler.setAdapter(adapter);
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public <T extends SwipeRecyclerAdapter> void setAdapter(T adapter) {
        adapter.setPlaceholder(placeholder);
        swipeRecycler.setAdapter(adapter);
    }


    /**
     * 关闭侧滑
     */
    public void closeSwipe(){
        swipeRecycler.closeSwipe();
    }

    /**
     * 设置占位图
     *
     * @param type  类型
     * @param resId 资源id
     */
    public void setPlaceholder(int type, int resId) {
        placeholder.setPlaceholder(type, resId);
    }

    private class DefAdapter extends RecyclerAdapter<String> {

        public DefAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutResId(int viewType) {
            return listItem == 0 ? R.layout.ui_core_item : listItem;
        }

        @Override
        protected void onItemBindViewHolder(ViewHolder holder, int position) {
            if (listItem == R.layout.ui_core_item) {
                holder.find(TextView.class, R.id.tv_item).setText("item - " + position);
            }
        }
    }

}
