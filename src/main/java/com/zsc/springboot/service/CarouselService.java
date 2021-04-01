package com.zsc.springboot.service;

import com.zsc.springboot.entity.Carousel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsc.springboot.vo.CarouselMapVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HGT
 * @since 2021-03-19
 */
public interface CarouselService extends IService<Carousel> {

    Integer addCarousel(String imgUrl,String address);
    List<CarouselMapVo> getCarouselMapList();
    Integer deleteCarousel(Integer id);

}
