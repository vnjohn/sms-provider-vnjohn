package org.vnjohn.sms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author vnjohn
 * @since 2023/3/17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractSMSShortLink {
    /**
     * 原始链接地址
     */
    protected String sourceUrl;
}