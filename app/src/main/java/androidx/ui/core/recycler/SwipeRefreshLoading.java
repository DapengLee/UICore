package androidx.ui.core.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
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

    private SwipeRefreshLayout swipeRefresh;
    private NestedScrollView scroll_view;
    private SwipeRecyclerView swipeRecycler;
    private RecyclerView recycler;
    private SwipeLoadingLayout loadingLayout;
    private Placeholder placeholder;

    private int itemCount;
    private int listItem;

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
        loadingLayout.attachNestedScrollView(scroll_view);
    }

    /**
     * 初始化
     */
    private void initView() {
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        scroll_view = (NestedScrollView) findViewById(R.id.scroll_view);
        swipeRecycler = (SwipeRecyclerView) findViewById(R.id.swipe_recycler);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        loadingLayout = (SwipeLoadingLayout) findViewById(R.id.loading_layout);
        placeholder = (Placeholder) findViewById(R.id.placeholder);
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
        return scroll_view;
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
    public SwipeLoadingLayout getSwipeLoadingLayout() {
        return loadingLayout;
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
        loadingLayout.setLoading(loading);
    }

    /**
     * 设置加载更多
     *
     * @param listener 加载更多监听
     */
    public void setOnLoadingListener(SwipeLoadingLayout.OnLoadingListener listener) {
        loadingLayout.setOnLoadingListener(listener);
    }

    /**
     * 设置布局管理器
     *
     * @param manager 布局管理器
     */
    public void setLayoutManager(LinearLayoutManager manager) {
        recycler.setLayoutManager(manager);
    }

    /**
     * 设置侧滑布局管理器
     *
     * @param manager 布局管理器
     */
    public void setSwipeLayoutManager(LinearLayoutManager manager) {
        swipeRecycler.setLayoutManager(manager);
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(RecyclerAdapter adapter) {
        adapter.setPlaceholder(placeholder);
        recycler.setAdapter(adapter);
    }

    /**
     * 设置侧滑适配器
     *
     * @param adapter 侧滑适配器
     */
    public void setSwipeAdapter(SwipeRecyclerAdapter adapter) {
        adapter.setPlaceholder(placeholder);
        swipeRecycler.setAdapter(adapter);
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
