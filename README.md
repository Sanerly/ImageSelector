# ImageSelector

 - 多选
 ![描述](http://thyrsi.com/t6/369/1536545675x-1566663106.gif)
 - 单选
![描述](http://thyrsi.com/t6/369/1536545699x-1566663106.gif)

主要功能
--

 - 图片缩放
 - 图片裁剪
 - 支持单选和多选
 - 可修改裁剪框大小
 - 可修改选择器列数
 - 使用观察者模式返回图片集合
 - 可自定义定义图片加载框架(Gilde、Picasso) 
 - 可自定义定义选择器顶部toolbar的背景色

使用方式
--

 - 在项目目录下build.gradle中添加
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
 - 在app目录下build.gradle中添加

```
implementation 'com.github.Sunsetzp:ImageSelector:1.01'
```

 - 创建配置
 

```
  SelConf mConf = new SelConf.Builder()
                 .setMaxCount(9)//多选状态下最多可选的数量
                .setMultiSelected(true) //多选-->true 单选-->false
                .setColumns(3) //选择器中每行显示的列数
                .setClip(true) //是否裁剪
                .setBorderSize(0.7f) //裁剪的图片的大小，范围大是0-->1.0。占屏幕宽度的百分比
                .setObserver(String.valueOf(RESULT_OK), iObserver) //设置完成之后返回的图片集合
                .setImageloader(imageloader) //设置图片加载框架
                .setToolbarBackground(R.color.toolbarBackground) //设置顶部toolbar背景色
                .build();
  //启动选择器             
  ISMain.getInstance().startSelectedActivity(MainActivity.this, mConf);
```

 - 返回数据集合的接口
 
```
    IObserver iObserver = new IObserver() {
        @Override
        public void ObserverUpdate(String key, final Object obj) {
            if (!key.equals(String.valueOf(RESULT_OK))) return;
            final ArrayList<String> sparseArray = (ArrayList<String>) obj;
            for (int i = 0; i < sparseArray.size(); i++) {
                LogUtil.logd("key = " + key + "   value = " + sparseArray.get(i));
                ImageView imageView = new ImageView(MainActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(params);
                loader(MainActivity.this, imageView, sparseArray.get(i));
                final int pos = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //启动裁剪页
                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add(sparseArray.get(pos));
                        ISMain.getInstance().startClipImageActivity(MainActivity.this, arrayList, mConf);
                    }
                });
                linearLayout.addView(imageView);
            }
        }
    };
```
 - 设置图片加载框架接口

```
    Imageloader imageloader = new Imageloader() {
        @Override
        public void load(Context context, ImageView image, String path) {
            loader(context, image, path);
        }

    };

    private void loader(Context context, ImageView image, String path) {
        Glide.with(context)
                .load(path)
                .into(image);
    }
```

如果是单独启动裁剪页面，只需要配置Imageloader 和IObserver 、图片裁剪框大小即可。
