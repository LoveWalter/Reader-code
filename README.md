# Reader-code
Android Reader Code
多功能小说阅读器，客户端和服务器端。Reader-Code是客户端的代码
登录注册需要和服务器数据交互
使用界面通过TabHost创建了4个标签，每个标签有相应的Activity
最近阅读使用GridView网格布局，数据来源是保存xml中的数据
本地浏览遍历特定格式的文件，重写BaseAdapter将数据填充在ListView，OnClieckListener和OnItemLongClickListener
网上书城通过WebView实现，调用系统内的下载接口Downloader
个人书籍下载通过多任务多线程断点续传来实现
书籍阅读通过贝赛尔曲线实现翻页效果，PopuoWindow呈现字体大小，阅读亮度，书签管理，跳转进度
