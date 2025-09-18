package com.java.web.base.req;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Kevin
 * @date 2024/4/20 17:13
 */

@Data
@NoArgsConstructor
public class ReqUser implements Serializable {

    private Long userId;

}
