package org.vnjohn.sms.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import org.vnjohn.sms.enums.SMSSignPurposeEnum;
import org.vnjohn.sms.enums.SMSSignSourceEnum;
import org.vnjohn.sms.enums.SMSSignTypeEnum;
import org.vnjohn.sms.enums.tencent.TencentDocumentTypeEnum;

import java.io.Serializable;

/**
 * @author vnjohn
 * @since 2023/3/17
 */
@Data
public class ModifySignDTO implements Serializable {
    private static final long serialVersionUID = -2398445383448016386L;

    /**
     * 签名 ID
     */
    private Long id;

    /**
     * 签名名称
     */
    private String name;

    /**
     * 备注/说明
     */
    private String remark;

    /**
     * 签名来源
     * {@link SMSSignSourceEnum}
     */
    private Integer source;

    /**
     * 认证文件，图片
     */
    private MultipartFile file;

    /**
     * 签名类型
     * {@link SMSSignTypeEnum}
     */
    private Integer type;

    /**
     * 认证类型
     * {@link TencentDocumentTypeEnum}
     */
    private Integer certificationType;

    /**
     * 签名用途
     * {@link SMSSignPurposeEnum}
     */
    private Integer purpose;

}
