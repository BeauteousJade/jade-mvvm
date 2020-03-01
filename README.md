# jade-mvvm
# 1. mvvm的分层
&emsp;&emsp;整个mvvm的分层参考于官方的介绍--[应用架构指南](https://developer.android.com/jetpack/docs/guide),整个结构也符合如下图所示：

![](https://upload-images.jianshu.io/upload_images/9124992-cd678a735f59de56.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

&emsp;&emsp;相比于官方推荐的结构，我在此基础扩展了两个点：
>1. View的职责分离。如果按照官方的推荐，View相关的操作必须在View层，这就意味着很多业务将耦合在View里面，可能会导致`Activity`和`Fragment`,为了减轻`Activity`和`Fragment`的负担，同时为了将相关职责分离和整合，我在从MVP架构里面吸取了相关经验，将View拆分为了多个Presenter，每个Presenter只负责自己相关的业务即可。需要注意的是Presenter仍然属于View层。
>2. 将repository整合成一个。按照官方的推荐，repository应该明确区分，比如说，从内存中加载数据的repository和从网络上加载数据的repository应该是不一样的。而我认为，这两个repository在上层的逻辑都是一样的，唯一的区别就是数据的来源，所以我在repository层做了统一，统一从request层去获取数据，由request层去决定数据的来源。

&emsp;&emsp;所以mvvm框架总结下来，便是如下的结构：
![](https://upload-images.jianshu.io/upload_images/9124992-c80eb7daf49bbe92.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 2. 基本使用
&emsp;&emsp;在mvvm框架里面，每个页面都有Activity+Fragment组成的，Activity本身不会承载很多的业务，所有业务都应该收敛在Fragment里面。
### (1).Activity和Fragment的创建
&emsp;&emsp;每个Activity必须直接或者间接继承于BaseActivity，每个Fragment必须直接或者间接继承于BaseFragment。通常情况下，Activity只负责加载一个Fragment即可，当然也可以处理页面上的事情，比如说，window的feature和theme等。

&emsp;&emsp;下面展示一个简单的例子(可以从[jade-mvvm](https://github.com/BeauteousJade/jade-mvvm)获得完整的代码)：
```
class MessageDetailActivity : BaseActivity() {

    private val mMessageId by ExtraDelegate(MESSAGE_ID, 0)

    override fun buildCurrentFragment() = MessageDetailFragment.newInstance()

    override fun buildFragmentArguments() = Bundle().apply {
        putInt(MESSAGE_ID, mMessageId)
    }

    companion object {
        const val MESSAGE_ID = "MESSAGE_ID"
    }
}
```
&emsp;&emsp;上面便是一个Activity的代码，它的工作主要加载一个Fragment，我们来看看对应的Fragment的代码：
```
class MessageDetailFragment : BaseFragment<MessageDetailViewModel>() {

    private val mMessageId by ExtraDelegate(MessageDetailActivity.MESSAGE_ID, 0)

    override fun getLayoutId() = R.layout.fragment_message_detail

    override fun onCreateViewModel() =
        ViewModelProvider(this, MessageDetailViewModel.Factory(mMessageId))[MessageDetailViewModel::class.java]

    override fun onCreatePresenter() = Presenter().apply {
        addPresenter(MessageDetailRefreshPresenter())
        addPresenter(MessageDetailInitViewPresenter())
    }

    companion object {
        fun newInstance() = MessageDetailFragment()
    }
}
```
&emsp;&emsp;Fragment做的事情相对来说就比较多了，首先是创建一个自己的ViewModel，其次将一些相关业务抽到不同的Presenter里面。我这里将只是简单的定义了两个Presenter，其中:`MessageDetailRefreshPresenter`用来负责刷新相关操作，`MessageDetailInitViewPresenter`负责将加载回来的数据展示到View层。
> 需要特别注意的是：Presenter本身属于View层的一部分，所以请勿做一些非View层的事情，比如说请求数据。如果想要请求数据，应当通知ViewModel，让ViewModel去请求，Presenter此时只需要做一件事，监听对应的LiveData，等待数据更新在更新View层即可。

&emsp;&emsp;例如，`MessageDetailRefreshPresenter`的代码将展示如何正确的请求数据：
```
class MessageDetailRefreshPresenter : Presenter() {

    @Inject(Constant.VIEW_MODEL)
    lateinit var mMessageDetailViewModel: MessageDetailViewModel

    private val mRefreshLayout by lazy {
        getRootView().findViewById<SwipeRefreshLayout>(R.id.refresh_layout)
    }

    override fun onBind() {
        mMessageDetailViewModel.mLoadStatusLiveData.observe(getCurrentFragment()!!, Observer<LoadStatus> {
            mRefreshLayout.isRefreshing = it == LoadStatus.LOADING_REFRESH
        })
        mRefreshLayout.setOnRefreshListener {
            mMessageDetailViewModel.refresh()
        }
        mMessageDetailViewModel.trRefresh()
    }
}
```
### (2).如何正确创建一个ViewModel
&emsp;&emsp;前面已经说了， 每个Fragment需要一个ViewModel。所以在创建好一个Fragment之后，就需要定义一个与之对应的ViewModel。在mvvm框架里面，已经定义两种ViewModel：`BaseViewModel`和`BaseRecyclerViewModel`。其中`BaseViewModel`用于普通Fragment；`BaseRecyclerViewModel`用于带有RecyclerView的Fragment，推荐这类Fragment继承于`RecyclerViewFragment`，其中`BaseRecyclerViewModel`集成了jetpack组件里面的paging库，所以使用`BaseRecyclerViewModel`,网络加载这块就不需要我们过多的关心。

&emsp;&emsp;为了介绍的简单，本文将以`BaseViewModel`为例，简单介绍它的基本使用。如果想要了解`BaseRecyclerViewModel`,可以看[PositionViewModel](https://github.com/BeauteousJade/jade-mvvm/blob/master/app/src/main/java/com/jade/jade_mvvm/viewModel/PositionViewModel)和[PositionViewModel.kt](https://github.com/BeauteousJade/jade-mvvm/blob/master/app/src/main/java/com/jade/jade_mvvm/viewModel/PositionViewModel.kt)。

&emsp;&emsp;要想创建一个ViewModel，首先要继承于`BaseViewModel`：
```
class MessageDetailViewModel(id: Int) : BaseViewModel<Message>(MessageDetailRepository(id)) {

    class MessageDetailRepository(private val id: Int) : BaseRepository<Message>() {
        override fun getRequest() = MessageDetailRequest(id)
    }

    class Factory(private val id: Int) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>) = MessageDetailViewModel(id) as T
    }
}
```
&emsp;&emsp;创建一个ViewModel之后，我们还需要给ViewModel创建一个`Repository`对象，这个`Repository`对象的作用主要是加载数据，就如前文所说，`Repository`里面主要从`Request`层获取数据，这个数据可以从本地获取，也可以从网络上获取，这个不是ViewModel和Repository关心的。

&emsp;&emsp;在mvvm框架里面定义三个`Repository`接口，分别是：
>1. ItemKeyedRepository:主要是跟`BaseItemKeyedDataSource`搭配使用。
>2. ListPositionRepository:主要是跟`BasePositionDataSource`搭配使用。
>3. Repository:通用的接口，只要不符合上面两种情况，均可以使用。

&emsp;&emsp;在ViewModel中，除了定义`Repository`，每个实现者都可以按需定义很多的LiveData，用来达到的业务要求。关于LiveData的基本使用以及需要注意的事项，可以参考Google爸爸的博客：
>1. [LiveData 概览](https://developer.android.com/topic/libraries/architecture/livedata)
>2. [ViewModels and LiveData: Patterns + AntiPatterns](https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54)
>3. [LiveData beyond the ViewModel — Reactive patterns using Transformations and MediatorLiveData](https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7)
>4. [LiveData with SnackBar, Navigation and other events (the SingleLiveEvent case)](https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150)
