package com.zsc.springboot.service.impl;

import com.zsc.springboot.entity.Carousel;
import com.zsc.springboot.mapper.CarouselMapper;
import com.zsc.springboot.service.CarouselService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.springboot.vo.CarouselMapVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HGT
 * @since 2021-03-19
 */
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel> implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Transactional
    @Override
    public Integer addCarousel(String imgUrl, String address) {
        Carousel carousel = new Carousel();
        carousel.setImage(imgUrl);
        carousel.setAddress(address);
        return carouselMapper.insert(carousel);
    }

    @Override
    public List<CarouselMapVo> getCarouselMapList() {
        return carouselMapper.getCarouselMapList();
    }

    @Override
    public Integer deleteCarousel(Integer id) {
        return carouselMapper.deleteCarousel(id);
    }
}
