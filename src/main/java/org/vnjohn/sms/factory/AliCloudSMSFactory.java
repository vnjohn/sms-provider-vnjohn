package org.vnjohn.sms.factory;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.vnjohn.sms.condition.AliCloudOnCondition;
import org.vnjohn.sms.dto.ApplySignDTO;
import org.vnjohn.sms.dto.ApplyTemplateDTO;
import org.vnjohn.sms.dto.ModifySignDTO;
import org.vnjohn.sms.dto.ModifyTemplateDTO;
import org.vnjohn.sms.entity.AbstractSMSShortLink;
import org.vnjohn.sms.entity.AbstractSMSSign;
import org.vnjohn.sms.entity.AbstractSMSTemplate;
import org.vnjohn.sms.entity.ali.AliApplyOrModifySign;
import org.vnjohn.sms.entity.ali.AliRemoveSign;
import org.vnjohn.sms.entity.ali.AliStatusSign;
import org.vnjohn.sms.enums.ali.AliSignSourceEnum;
import org.vnjohn.sms.enums.ali.AliSignTypeEnum;
import org.vnjohn.sms.utils.CertificationFileUtil;
import org.vnjohn.sms.utils.JacksonUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.vnjohn.sms.SmsBusinessException.throwNull;

/**
 * @author vnjohn
 * @since 2023/3/17
 */
@Component
@Conditional(AliCloudOnCondition.class)
public class AliCloudSMSFactory extends AbstractSMSFactory {
    private static final String FILE_SUFFIX = "FileSuffix";
    private static final String FILE_CONTENT = "FileContents";

    @Override
    public <T extends AbstractSMSSign> AbstractSMSSign createApplySign(ApplySignDTO applySignDTO) {
        return getAbstractSMSSign(applySignDTO.getSource(), applySignDTO.getType(), applySignDTO.getFile(), applySignDTO.getName(), applySignDTO.getRemark());
    }

    @Override
    public <T extends AbstractSMSSign> AbstractSMSSign createModifySign(ModifySignDTO modifySignDTO) {
        return getAbstractSMSSign(modifySignDTO.getSource(), modifySignDTO.getType(), modifySignDTO.getFile(), modifySignDTO.getName(), modifySignDTO.getRemark());
    }

    private AbstractSMSSign getAbstractSMSSign(Integer source, Integer type, MultipartFile file, String name, String remark) {
        AliSignSourceEnum signSourceEnum = checkAliSignSource(source);
        AliSignTypeEnum signTypeEnum = checkAliSignType(type);
        String fileContent = processFileContent(file);
        return AliApplyOrModifySign.builder()
                                   .name(name)
                                   .source(signSourceEnum.getOutCode())
                                   .type(signTypeEnum.getCode())
                                   .fileList(fileContent)
                                   .remark(remark)
                                   .build();
    }

    @Override
    public <T extends AbstractSMSSign> AbstractSMSSign createRemoveSign(Long signId, String signName) {
        return AliRemoveSign.builder().name(signName).build();
    }

    @Override
    public <T extends AbstractSMSSign> AbstractSMSSign createQuerySignStatus(Long signId, String signName) {
        return AliStatusSign.builder().name(signName).build();
    }

    @Override
    public <T extends AbstractSMSTemplate> AbstractSMSSign createApplyTemplate(ApplyTemplateDTO applyTemplateDTO) {
        return super.createApplyTemplate(applyTemplateDTO);
    }

    @Override
    public <T extends AbstractSMSTemplate> AbstractSMSSign createModifyTemplate(ModifyTemplateDTO modifyTemplateDTO) {
        return super.createModifyTemplate(modifyTemplateDTO);
    }

    @Override
    public <T extends AbstractSMSTemplate> AbstractSMSSign createRemoveTemplate() {
        return super.createRemoveTemplate();
    }

    @Override
    public <T extends AbstractSMSTemplate> AbstractSMSSign createQueryTemplateStatus() {
        return super.createQueryTemplateStatus();
    }

    @Override
    public <T extends AbstractSMSShortLink> AbstractSMSSign createShortLink() {
        return super.createShortLink();
    }

    @NotNull
    private AliSignTypeEnum checkAliSignType(Integer signType) {
        AliSignTypeEnum signTypeEnum = AliSignTypeEnum.parseByCode(signType);
        throwNull(signTypeEnum, "签名类型不存在");
        return signTypeEnum;
    }

    @NotNull
    private AliSignSourceEnum checkAliSignSource(Integer signSource) {
        AliSignSourceEnum signSourceEnum = AliSignSourceEnum.parseByInnerCode(signSource);
        throwNull(signSourceEnum, "签名来源不存在");
        return signSourceEnum;
    }

    /**
     * 处理文件内容
     *
     * @param file
     * @return
     */
    private String processFileContent(MultipartFile file) {
        String fileSuffix = CertificationFileUtil.getFileSuffix(file);
        String fileContent = CertificationFileUtil.encryptFileToBase64(file);
        Map<String, String> fileContentMap = new HashMap<>(2);
        fileContentMap.put(FILE_SUFFIX, fileSuffix);
        fileContentMap.put(FILE_CONTENT, fileContent);
        return JacksonUtils.toJson(Collections.singleton(fileContentMap));
    }
}
