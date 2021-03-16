package com.arno.grow.user.web.model.req;

import java.io.Serializable;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午11:03
 * @version:
 */
public class ConverterRequest implements Serializable {
    private static final long serialVersionUID = 8528836179150791801L;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
