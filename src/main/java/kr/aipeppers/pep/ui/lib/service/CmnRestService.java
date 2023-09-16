package kr.aipeppers.pep.ui.lib.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.aipeppers.pep.core.cont.CmnConst;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.core.util.ConfigUtil;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.crypto.AesUtil;
import kr.aipeppers.pep.core.util.crypto.Base64Util;
import kr.aipeppers.pep.core.util.crypto.SeedUtil;
import kr.aipeppers.pep.core.util.file.FileUtil;
import kr.aipeppers.pep.ui.lib.controller.CmnRestController.ReqUploadDto;
import kr.aipeppers.pep.ui.lib.domain.ReqPrflImgUldDto;
import kr.aipeppers.pep.ui.mypage.service.MyinfoService;
import lombok.extern.slf4j.Slf4j;

/**
 * 공통 rest 서비스
 *
 */
@Slf4j
@Service
public class CmnRestService {

	@Autowired
	protected SqlSessionTemplate dao;

	static String charset = "utf-8";
	/**
	 * 프로필 이미지(base64) 업로드
	 * @param reqDto
	 * @return
	 */
	public Box prflImgUld(ReqPrflImgUldDto reqDto) {
		Box sBox = SessionUtil.getUserData();
		if (sBox == null || sBox.nvl("userId").equals("") ) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		Box inputBox = BoxUtil.toBox(reqDto);
		inputBox.put("mnmPrflNcknme", inputBox.nvl("mnmPrflNcknme"));
		inputBox.put("mnmPrflImgCn", Base64Util.decode(inputBox.nvl("mnmPrflImgCn").replaceFirst("data:image/.*;base64,", CmnConst.STR_EMPTY).getBytes())); //프로필이미지내용
		inputBox.put("sBox", sBox);

		Box resultBox = new Box();
//		resultBox.put("cnt", dao.insert("cmn.prflInsert", inputBox));

		return resultBox;
	}

	/**
	 * 디바이스 토큰 사용자 정보 조회
	 * @param box
	 * @return
	 */
	public Box tokenUserInfoView(Box box)  throws Exception {
		Box resBox = new Box();
		//디비에 저장된 api 셋팅값 가져옴
		String[] title = {"CRYPT_PASS" , "CRYPT_IV" , "G_BSZUSER_KEY" , "G_BSZIV"};
		box.put("title", title);
		List<Box> apiData = dao.selectList("cmn.apiSettingList" , box);
		String AesKey = "";
		String AesIV = "";

		String[] SeedKey = null;
		String[] SeedIV = null;

		//api 데이터 나눔
		for(Box data : apiData) {
			if(data.get("title").equals("CRYPT_PASS")) {
				AesKey = data.get("value").toString();
				continue;
			}

			if(data.get("title").equals("CRYPT_IV")) {
				AesIV = data.get("value").toString();
				continue;
			}

			if(data.get("title").equals("G_BSZUSER_KEY")) {
				SeedKey = data.get("value").toString().split(",");
				continue;
			}

			if(data.get("title").equals("G_BSZIV")) {
				SeedIV = data.get("value").toString().split(",");
				continue;
			}
		}
		byte[] SeedKeyByte = new byte[SeedKey.length] ;
		byte[] SeedIVByte = new byte[SeedIV.length] ;

		//10진수 변환
		for(int i=0 ; i<16; i++) {
			SeedKeyByte[i] = (byte) Integer.parseInt( SeedKey[i], 16);
			SeedIVByte[i] = (byte) Integer.parseInt(SeedIV[i], 16);
		}

		//AesUtil 디코딩 후
		String aesDecodeData = AesUtil.decrypt(box.get("encValue").toString(), AesKey ,AesIV );
		//byte 배열에 넣어줌
		byte[] text = new byte[aesDecodeData.length()];
		for(int i =0 ;i<aesDecodeData.length();i++ ) {
			char ch = aesDecodeData.charAt(i);
			int num = (int) ch;
			text[i] = (byte) num;
		}
		//seed 인코딩 후
		byte[] seedEncodeData = SeedUtil.encrypt(text , SeedKeyByte ,SeedIVByte , null);

        Base64.Encoder encoder = Base64.getEncoder() ;
        byte[] encArr = encoder.encode(seedEncodeData);
        String encTxt = new String(encArr , "utf-8");

        box.put("encTxt", encTxt);
        //user 테이블 디바이스 토큰 조회
        resBox.put("user", dao.selectOne("cmn.tokenUserInfoView", box));
		return resBox;
	}


	/**
	 * 파일 업로드 테스트
	 * @param reqDto
	 * @return
	 */
	public Box fileUploadService(ReqUploadDto reqDto)  throws Exception {
		if("".equals(reqDto.getFileType())) {
			throw new BizException("E107", new String[] {"파일 타입이"}); // {0} 존재하지 않습니다.
		}else if(! ("image".equals(reqDto.getFileType()) || "post".equals(reqDto.getFileType()) || "video".equals(reqDto.getFileType())) ){
			throw new BizException("F112", new String[] {"올바른 값을 입력해주세요."} );
		}
		MultipartFile files[] = (MultipartFile[]) reqDto.getFiles();
		if(files == null) {
			throw new BizException("E107", new String[] {"첨부 파일이"}); // {0} 존재하지 않습니다.
		}else {
			if("image".equals(reqDto.getFileType()) && files.length >=2) {
				throw new BizException("E103", new String[]{"1"}); //첨부가능 파일수({0})를 초과했습니다.
			}

			Box resBox = new Box();

			if("image".equals(reqDto.getFileType()) || "post".equals(reqDto.getFileType()) ){ //post
				//이미지 인 경우에만 파일 저장 후
				resBox = fileUpload(files);

				//아래 결과에 경로가 있음
				List<Box> bb = (List<Box>) resBox.get("list");
				for(Box b :bb) {
					String path  = b.get("path").toString();
					System.out.println(path);
				}
			}else {	//동영상인 경우 경로만 저장

			}

			return resBox;
		}
	}


	public static Box fileUpload(MultipartFile[] mFileList) throws Exception {
		Box box = new Box();

		if (ConfigUtil.getInt("file.max.cnt") < mFileList.length) {
			throw new BizException("E103", new String[]{ConfigUtil.getString("file.max.cnt")}); //첨부가능 파일수({0})를 초과했습니다.
		}

		List<Box> fileList = new ArrayList<Box>();
		List<Box> fileExistList = new ArrayList<Box>();

		for (MultipartFile mFile : mFileList) {
			if (!mFile.isEmpty()) {
				String fileKind = "F";
				String fileExt = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf(".") + 1, mFile.getOriginalFilename().length()).toLowerCase();
				if (mFile.getContentType().startsWith("image")) {
					fileKind = "I";
				}
				String allowExt = fileKind.equals("I") ? ConfigUtil.getString("file.img.ext") : ConfigUtil.getString("file.ext");
				if (allowExt.indexOf(fileExt) <= -1) {
					throw new BizException("E104", new String[]{allowExt, fileExt}); //허용({0})되지 않은 첨부파일({1}) 형식입니다.
				}

				int fileMaxSize = fileKind.equals("I") ? ConfigUtil.getInt("file.max.size") : ConfigUtil.getInt("file.max.size");
				if (fileMaxSize < mFile.getSize()) {
					throw new BizException("E105", new String[]{(fileMaxSize / 1000 / 1000) + "Mb"}); //첨부가능 용량({0})을 초과했습니다 .
				}
			}

			boolean succFlag = true;
			String fullPath = "";
			String sysFileNm = UUID.randomUUID().toString().replace("-", "");
			box.put("fileNo", sysFileNm);

			try {
				//에러처리용 객체
				Box rowBox = new Box();
				rowBox.put("tmpFileNm", sysFileNm);
				fileExistList.add(rowBox);

				String fileExt = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf(".") + 1, mFile.getOriginalFilename().length()).toLowerCase();

				String filePath = ConfigUtil.getString("file.upload.img.path")+"/post";
				fullPath = filePath;
				FileUtil.makeDirectory(fullPath);
				File uploadFile = new File(fullPath, sysFileNm+"."+fileExt);
				try {
					uploadFile.createNewFile();
				} catch (IOException e) {
					log.error("{}", e);
					succFlag = false;
					throw new BizException("E102"); //첨부파일 등록이 실패하였습니 다.
				}
				try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(uploadFile));
					BufferedInputStream bis = new BufferedInputStream(mFile.getInputStream())) {
					byte[] buffer = new byte[4096 * 2];
					int read = 0;
					while ((read = bis.read(buffer)) != -1) {
						bos.write(buffer, 0, buffer.length);
					}
					bos.flush();
					bis.close();
				} catch (Exception e) {
					log.error("{}", e);
					succFlag = false;
					throw new BizException("E102"); //첨부파일 등록이 실패하였습니다.
				}

				//
			    BufferedImage bufferedImage = ImageIO.read(mFile.getInputStream());
			    int width = bufferedImage.getWidth();
			    int height = bufferedImage.getHeight();

				//클라이언트에 파일 업로드 정보를 바로 쓸수 있게  알려준다.
				rowBox = new Box();
				rowBox.put("fileSize", mFile.getSize());
				rowBox.put("fileNm", mFile.getOriginalFilename());
				rowBox.put("imageUrl", "/app/webroot/upload/images/post/" + mFile.getOriginalFilename());
				rowBox.put("width", width);
				rowBox.put("height", height);
				rowBox.put("imageSize", width + "*" + height);
				fileList.add(rowBox);

			} finally {
				if (!succFlag) {
					for (Box rowBox : fileExistList) {
						File file = new File(fullPath, rowBox.nvl("tmpFileNm"));
						FileUtil.fileDelete(file);
					}
				}
			}
		}

		Box resBox = new Box();
		resBox.put("list", fileList);
		return resBox;
	}

}
