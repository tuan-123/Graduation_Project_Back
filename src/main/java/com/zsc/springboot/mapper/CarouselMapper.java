package com.zsc.springboot.mapper;

import com.zsc.springboot.entity.Carousel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsc.springboot.vo.CarouselMapVo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HGT
 * @since 2021-03-19
 */
@Repository
public interface CarouselMapper extends BaseMapper<Carousel> {

    @Select("select id, image as img,address as url, create_time from carousel where deleted = 0 order by create_time desc")
    List<CarouselMapVo> getCarouselMapList();

    @Update("update carousel set deleted = 1 where id = #{id} limit 1")
    Integer deleteCarousel(Integer id);
}
