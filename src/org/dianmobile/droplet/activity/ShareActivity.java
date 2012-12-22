package org.dianmobile.droplet.activity;

import static org.dianmobile.droplet.constants.Constants.*;
import static org.dianmobile.droplet.db.HabitDb.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.db.HabitDb;
import org.dianmobile.droplet.models.Habit;
import org.dianmobile.droplet.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 谈感受按钮对应的页面
 * 
 * @author FreeTymeKiyan
 * @version 0.0.7
 */
public class ShareActivity extends Activity {
	/*控件*/
	/**返回上一页的按钮*/
	private ImageButton btnRtn;
	/**发布的按钮*/
	private ImageButton btnPublish;
	/**取消发布感受的按钮*/
	private Dialog cancelDlg;
	/**谈感受的输入框*/
	private EditText etShareFeeling;
	/**请人监督的按钮*/
	private Button btnAddFollowers;
	/**上传图片的按钮*/
	private Button btnAddPic;
	/*数据*/
	/**当前对应习惯的uuid*/
	private String strUuid = null;
	/**上传图片的绝对目录*/
	private String strPicPath = "";
	/**选择、拍摄图片之后得到的位图*/
	private Bitmap curBmp = null;
	/**谈感受的字符串*/
	private String strFeeling = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		/*从主页面获取习惯的UUID*/
		getData();
		initViews();
	}

	/**
	 * 获取主页面传过来的数据
	 */
	private void getData() {
		Intent i = getIntent();
		strUuid = i.getStringExtra(UUID);
//		System.out.println("uuid is: " + strUuid);
	}

	/**
	 * 初始化控件 
	 */
	private void initViews() {
		btnRtn = (ImageButton) findViewById(R.id.ib_shareRtn);
		btnRtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ifCancel();
			}
		});
		btnPublish = (ImageButton) findViewById(R.id.ib_publish);
		btnPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (judgeValidity()) { // 有文字内容
					if (curBmp != null) {
						saveBmp(curBmp); // 存到应用缓存目录下
					}
					/*把对应信息插入数据库*/
					HabitDb hd = new HabitDb(ShareActivity.this);
					ContentValues cv = new ContentValues();
					cv.put(UUID, strUuid);
					cv.put(WORDS, strFeeling);
					cv.put(PIC_PATH, strPicPath);
					Habit h = new Habit(cv);
					if (hd.insertLog(h)) { // 插入成功
						Toast.makeText(ShareActivity.this, 
								R.string.toast_insert_success, 
								Toast.LENGTH_SHORT).show();
						setResult(RESULT_OK);
						finish();
					} else {
						Toast.makeText(ShareActivity.this, 
								R.string.toast_insert_failure, 
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		etShareFeeling = (EditText) findViewById(R.id.et_shareFeeling);
		btnAddFollowers = (Button) findViewById(R.id.btn_addShareFollowers);
		btnAddFollowers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 跳转到好友列表
				// TODO 从好友列表当中选择要@的人
			}
		});
		btnAddPic = (Button) findViewById(R.id.btn_addPic);
		btnAddPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*判断是否有sdcard存在*/
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// SD卡存在
					AlertDialog.Builder adb = new AlertDialog.Builder(
							ShareActivity.this);
					adb.setTitle(R.string.choose_source);
					adb.setItems(R.array.picSource, 
							new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							switch (which) {
							case CHOOSE_PICTURE_FROM_ALBUM:
								Intent getImage = 
										new Intent(Intent
										.ACTION_GET_CONTENT);
								getImage.addCategory(Intent
										.CATEGORY_OPENABLE);
								getImage.setType("image/jpeg");
								startActivityForResult(getImage, 
										CHOOSE_PICTURE_FROM_ALBUM);
								break;
							case CHOOSE_PICTURE_FROM_CAMERA:
								Intent getImageByCamera = 
										new Intent("android.media" +
										".action.IMAGE_CAPTURE");
								startActivityForResult(
										getImageByCamera, 
										CHOOSE_PICTURE_FROM_CAMERA);
								break;
							default:
								break;
							}
						}
					});
					adb.create().show();
				} else { // SD卡不存在
					Toast.makeText(ShareActivity.this, R.string
							.toast_sdcard_not_found, Toast.LENGTH_SHORT)
							.show();
				}
				
			}
		});
	}
	
	/**
	 * 是否取消发布内容
	 */
	private void ifCancel() {
		AlertDialog.Builder builder = new AlertDialog
				.Builder(this);
		builder.setTitle(R.string.cancel_share)
				.setMessage(R.string.cancel_share_msg)
				.setPositiveButton(R.string.yes, 
						new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setResult(RESULT_CANCELED);
				finish();
			}
		}).setNegativeButton(R.string.no, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancelDlg.dismiss();
			}
		});
		cancelDlg = builder.create();
		cancelDlg.show();
	}
	
	/**
	 * 截取返回键点击事件，显示对话框
	 */
	@Override
	public void onBackPressed() {
		ifCancel();
	}
	
	/**
	 * 判断输入内容是否合法
	 * 
	 * @return	true	输入框中有内容，输入合法
	 * 			false	输入不合法
	 */
	private boolean judgeValidity() {
		boolean validity = false;
		strFeeling = etShareFeeling.getText().toString();
		if (!strFeeling.equals("")) {
			validity = true;
		}
		return validity;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CHOOSE_PICTURE_FROM_ALBUM:
			switch (resultCode) {
			case RESULT_OK:
				if (data != null) {
					htcSetBackground(data);
				} else {
					otherSetBackground(data);
				}
				break;
			case RESULT_CANCELED:

			default:
				break;
			}
			break;
		case CHOOSE_PICTURE_FROM_CAMERA:
			switch (resultCode) {
			case RESULT_OK:
				if (data != null) {
					htcSetBackground(data);
				} else {
					otherSetBackground(data);
				}
				break;
			case RESULT_CANCELED:

			default:
				break;
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 根据返回的图片给上传图片的区域绑定选好的图片
	 * 非HTC机型
	 */
	private void otherSetBackground(Intent data) {
		curBmp = (Bitmap) (data.getExtras() == null ? 
				null : data.getExtras().get("data"));
		saveBmp(curBmp); // bmp -> file -> 文件夹
		// 把得到的图片绑定在控件上显示
		BitmapDrawable drawable = new BitmapDrawable(curBmp);
		btnAddPic.setBackgroundDrawable(drawable);
		btnAddPic.setText("");
	}

	/**
	 * 根据返回的图片给上传图片的区域绑定选好的图片
	 * HTC机型
	 */
	private void htcSetBackground(Intent data) {
		ContentResolver resolver = getContentResolver();
		Uri background = data.getData(); // 获得图片的uri
		try {
			byte[] mContent = readStream(resolver
					.openInputStream(Uri.parse(background
					.toString()))); // 将图片内容解析成字节数组
			// 将字节数组转换为ImageView可调用的Bitmap对象
			curBmp = getPicFromBytes(mContent, null);
			saveBmp(curBmp); // bmp -> file -> 文件夹
			// 把得到的图片绑定在控件上显示
			BitmapDrawable drawable = new BitmapDrawable(curBmp);
			btnAddPic.setBackgroundDrawable(drawable);
			btnAddPic.setText("");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 把图片存到应用文件夹的缓存目录下
	 */
	private void saveBmp(Bitmap bmp) {
		Utils.createSdCardDir(ShareActivity.this); // 创建目录
		strPicPath = Utils.getPicDir() + createPicName();
//		System.out.println("strPicPath:" + strPicPath);
		File f = new File(strPicPath);
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成图片的名字
	 * 
	 * @return String
	 */
	private String createPicName() {
		String temp = null;
		temp = System.currentTimeMillis() + ".jpg";
//		System.out.println("picName:" + temp);
		return temp;
	}

	/**
	 * 从byte得到bitmap的方法
	 * */
	private static Bitmap getPicFromBytes(byte[] bytes , BitmapFactory.Options opts ) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}
	
	/**
	 * 将输入流转化为byte的方法
	 * */
	private static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}
}