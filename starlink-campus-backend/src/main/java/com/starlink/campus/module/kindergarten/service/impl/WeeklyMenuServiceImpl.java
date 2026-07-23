package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgWeeklyMenu;
import com.starlink.campus.module.kindergarten.mapper.KgWeeklyMenuMapper;
import com.starlink.campus.module.kindergarten.service.WeeklyMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class WeeklyMenuServiceImpl extends ServiceImpl<KgWeeklyMenuMapper, KgWeeklyMenu> implements WeeklyMenuService {

    private static final Logger logger = LoggerFactory.getLogger(WeeklyMenuServiceImpl.class);

    @Override
    public Page<KgWeeklyMenu> listByWeek(LocalDate weekStart, Integer pageNum, Integer pageSize) {
        Page<KgWeeklyMenu> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<KgWeeklyMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(weekStart != null, KgWeeklyMenu::getWeekStartDate, weekStart)
               .orderByAsc(KgWeeklyMenu::getDayOfWeek)
               .orderByAsc(KgWeeklyMenu::getMealType);
        return this.page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMenu(KgWeeklyMenu menu) {
        menu.setPublishStatus("草稿");
        menu.setCreateTime(LocalDateTime.now());
        logger.info("Adding menu: {}", menu.getWeekStartDate());
        return this.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(KgWeeklyMenu menu) {
        logger.info("Updating menu id: {}", menu.getId());
        return this.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenu(Long id) {
        logger.info("Deleting menu id: {}", id);
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishMenu(Long id) {
        KgWeeklyMenu menu = this.getById(id);
        if (menu != null) {
            menu.setPublishStatus("已发布");
            logger.info("Publishing menu id: {}", id);
            return this.updateById(menu);
        }
        return false;
    }

    @Override
    public String checkAllergen(Long menuId, Long classId) {
        logger.info("Checking allergen for menu id: {} and class id: {}", menuId, classId);
        // Mock implementation
        return "未发现过敏原冲突";
    }
}
