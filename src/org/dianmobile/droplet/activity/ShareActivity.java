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
 * ̸���ܰ�ť��Ӧ��ҳ��
 * 
 * @author FreeTymeKiyan
 * @version 0.0.7
 */
public class ShareActivity extends Activity {
	/*�ؼ�*/
	/**������һҳ�İ�ť*/
	private ImageButton btnRtn;
	/**�����İ�ť*/
	private ImageButton btnPublish;
	/**ȡ���������ܵİ�ť*/
	private Dialog cancelDlg;
	/**̸���ܵ������*/
	private EditText etShareFeeling;
	/**���˼ල�İ�ť*/
	private Button btnAddFollowers;
	/**�ϴ�ͼƬ�İ�ť*/
	private Button btnAddPic;
	/*����*/
	/**��ǰ��Ӧϰ�ߵ�uuid*/
	private String strUuid = null;
	/**�ϴ�ͼƬ�ľ���Ŀ¼*/
	private String strPicPath = "";
	/**ѡ������ͼƬ֮��õ���λͼ*/
	private Bitmap curBmp = null;
	/**̸���ܵ��ַ���*/
	private String strFeeling = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		/*����ҳ���ȡϰ�ߵ�UUID*/
		getData();
		initViews();
	}

	/**
	 * ��ȡ��ҳ�洫����������
	 */
	private void getData() {
		Intent i = getIntent();
		strUuid = i.getStringExtra(UUID);
//		System.out.println("uuid is: " + strUuid);
	}

	/**
	 * ��ʼ���ؼ� 
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
				if (judgeValidity()) { // ����������
					if (curBmp != null) {
						saveBmp(curBmp); // �浽Ӧ�û���Ŀ¼��
					}
					/*�Ѷ�Ӧ��Ϣ�������ݿ�*/
					HabitDb hd = new HabitDb(ShareActivity.this);
					ContentValues cv = new ContentValues();
					cv.put(UUID, strUuid);
					cv.put(WORDS, strFeeling);
					cv.put(PIC_PATH, strPicPath);
					Habit h = new Habit(cv);
					if (hd.insertLog(h)) { // ����ɹ�
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
				// TODO ��ת�������б�
				// TODO �Ӻ����б���ѡ��Ҫ@����
			}
		});
		btnAddPic = (Button) findViewById(R.id.btn_addPic);
		btnAddPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*�ж��Ƿ���sdcard����*/
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// SD������
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
				} else { // SD��������
					Toast.makeText(ShareActivity.this, R.string
							.toast_sdcard_not_found, Toast.LENGTH_SHORT)
							.show();
				}
				
			}
		});
	}
	
	/**
	 * �Ƿ�ȡ����������
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
	 * ��ȡ���ؼ�����¼�����ʾ�Ի���
	 */
	@Override
	public void onBackPressed() {
		ifCancel();
	}
	
	/**
	 * �ж����������Ƿ�Ϸ�
	 * 
	 * @return	true	������������ݣ�����Ϸ�
	 * 			false	���벻�Ϸ�
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
	 * ���ݷ��ص�ͼƬ���ϴ�ͼƬ�������ѡ�õ�ͼƬ
	 * ��HTC����
	 */
	private void otherSetBackground(Intent data) {
		curBmp = (Bitmap) (data.getExtras() == null ? 
				null : data.getExtras().get("data"));
		saveBmp(curBmp); // bmp -> file -> �ļ���
		// �ѵõ���ͼƬ���ڿؼ�����ʾ
		BitmapDrawable drawable = new BitmapDrawable(curBmp);
		btnAddPic.setBackgroundDrawable(drawable);
		btnAddPic.setText("");
	}

	/**
	 * ���ݷ��ص�ͼƬ���ϴ�ͼƬ�������ѡ�õ�ͼƬ
	 * HTC����
	 */
	private void htcSetBackground(Intent data) {
		ContentResolver resolver = getContentResolver();
		Uri background = data.getData(); // ���ͼƬ��uri
		try {
			byte[] mContent = readStream(resolver
					.openInputStream(Uri.parse(background
					.toString()))); // ��ͼƬ���ݽ������ֽ�����
			// ���ֽ�����ת��ΪImageView�ɵ��õ�Bitmap����
			curBmp = getPicFromBytes(mContent, null);
			saveBmp(curBmp); // bmp -> file -> �ļ���
			// �ѵõ���ͼƬ���ڿؼ�����ʾ
			BitmapDrawable drawable = new BitmapDrawable(curBmp);
			btnAddPic.setBackgroundDrawable(drawable);
			btnAddPic.setText("");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * ��ͼƬ�浽Ӧ���ļ��еĻ���Ŀ¼��
	 */
	private void saveBmp(Bitmap bmp) {
		Utils.createSdCardDir(ShareActivity.this); // ����Ŀ¼
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
	 * ����ͼƬ������
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
	 * ��byte�õ�bitmap�ķ���
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
	 * ��������ת��Ϊbyte�ķ���
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