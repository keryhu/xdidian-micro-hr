package com.xdidian.keryhu.user.rest.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import com.xdidian.keryhu.service.imageService.ImageScaledService;
import com.xdidian.keryhu.user.domain.properties.ImageResizeProperties;
import com.xdidian.keryhu.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.xdidian.keryhu.user.config.CreateDir;
import com.xdidian.keryhu.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

/**
 * SecuredPostRest
 * 需要用户权限的，，post
 *
 * @author keryhu  keryhu@hotmail.com
 *         2016年9月3日 上午9:21:32
 */


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(ImageResizeProperties.class)
public class UploadUserHeaderRest {

    private final UserRepository repository;
    private final CreateDir createDir;
    private final ImageResizeProperties imageResizeProperties;
    private final String defaultImgType = ".png";

    private ImageScaledService imageScaledService = new ImageScaledService();


    /**
     * 因为用户头像和 上传的营业执照，或者 开通服务的委托书，不同，不需要图片里面的文字，清晰的表示，所以用户头像的像素不需要那么高
     * 且因为用户头像需要经常显示出来，而营业执照出现的频率很低，所以用户头像的体积，必需要压缩的很小。
     * resize 的规则
     * 1  体积大于1Mb，直接报错。
     * 2  体积大于500kb，且图像的像素大小默认的 1024*768，那么执行 resize，按照1024*768)
     *
     * @param uploadfile
     */

    @PostMapping("/users/personalInfo/uploadHeader")
    public ResponseEntity<?> uploadHeader(
            @RequestParam("uploadfile") MultipartFile uploadfile)
            throws IOException {

        // 验证file 的格式，
        int width = imageResizeProperties.getWidth();
        int height = imageResizeProperties.getHeight();
        long minResizeSize = imageResizeProperties.getMinResizeSize();
        long maxSize = imageResizeProperties.getMaxSize();

        //准备保存图片 的地址
        String userId = SecurityUtils.getCurrentLogin();
        String userHeaderImgPath = new StringBuffer(createDir.getUserHeader())
                .append("/")
                .append(userId)
                .append(defaultImgType)
                .toString();

        imageScaledService.resizeImageAndSave(uploadfile, width, height, minResizeSize, maxSize,
                userHeaderImgPath);


        Map<String, Boolean> map = new HashMap<String, Boolean>();

        map.put("result", true);
        //将图片 地址 保存到数据库
        repository.findById(userId).ifPresent(e -> {
            e.setHeaderPath(userHeaderImgPath);
            repository.save(e);
        });
        return ResponseEntity.ok(map);

    }

}
