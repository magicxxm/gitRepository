package com.mushiny.wms.masterdata.mdbasics.business;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.RandomUtil;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataGlobalDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemDataGlobalMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemDataGlobal;
import com.mushiny.wms.masterdata.mdbasics.domain.enums.Message;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataGlobalRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ItemDataGlobalBusiness {
    private static final Logger log = LoggerFactory.getLogger(ItemDataGlobalBusiness.class);
    private final ItemDataGlobalMapper itemDataGlobalMapper;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final ItemDataRepository itemDataRepository;
    private Pattern imgPattern = Pattern.compile("png");

    @Value("${mushiny.path}")
    private String picPath;

    @Autowired
    public ItemDataGlobalBusiness(ItemDataGlobalMapper itemDataGlobalMapper,
                                  ItemDataGlobalRepository itemDataGlobalRepository,
                                  ItemDataRepository itemDataRepository) {
        this.itemDataGlobalMapper = itemDataGlobalMapper;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.itemDataRepository = itemDataRepository;
    }

    public Message uploadSkuGlobal(MultipartFile file, Map<String, String> map, Message message){
        int result = 1;
        long size=file.getSize();
        String fileName = file.getOriginalFilename();
        String contentType = FilenameUtils.getExtension(fileName);
        Matcher matcher = imgPattern.matcher(contentType);
        if(size>3*1024*1024) {
            result = 0;
            message.setMessage("文件大于3MB");
            message.setResult(result);
            return message;
        }
        if (!matcher.matches()) {
            result = 0;
            message.setMessage("不支持文件类型" + contentType);
            message.setResult(result);
            log.info("文件类型不支持");
            return message;
        }
        //将MultipartFile转化为File
//        CommonsMultipartFile cf= (CommonsMultipartFile)file;
//        DiskFileItem df = (DiskFileItem)cf.getFileItem();
//        File f = df.getStoreLocation();
//        BufferedImage bi = null;
//        try {
//            bi = ImageIO.read(f);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int width = bi.getWidth();
//        int height = bi.getHeight();
//        if(width>1920 || height>1080){
//            result = 0;
//            message.setMessage("图片的分辨率大于1920×1080");
//            message.setResult(result);
//            log.info("图片分辨率大于设定值");
//        }
        ItemDataGlobal itemDataGlobal = saveItemDataGlobal(map);
        //上传路径
       // String path = "D:" + File.separator + "image" + File.separator;
        String saveFile = picPath + itemDataGlobal.getId() + "." + contentType;
        File uploadFile = new File(saveFile);
        //判断是否存在目录
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }
        try {
            FileUtils.touch(uploadFile);
            FileOutputStream out = new FileOutputStream(saveFile);
            BufferedOutputStream bs = new BufferedOutputStream(out);
            FileCopyUtils.copy(file.getInputStream(), bs);
        } catch (IOException e) {
            log.info("文件上传失败");
            throw new ApiException("文件上传失败");
        }
        //图片名称
        itemDataGlobal.setPictureName(itemDataGlobal.getId() + "." + contentType);
        //图片地址
        itemDataGlobal.setItemPicture(saveFile);
        itemDataGlobalRepository.save(itemDataGlobal);
        message.setResult(result);
        return message;
    }

    public ItemDataGlobal saveItemDataGlobal(Map<String, String> map) {
        ItemDataGlobalDTO dto = new ItemDataGlobalDTO();
        dto.setSkuNo(map.get("skuNo"));
        dto.setName(map.get("name"));
        dto.setDescription(map.get("description"));
        Object a = map.get("safetyStock");
        dto.setSafetyStock(((Integer) a).intValue());
        dto.setItemGroupId(map.get("itemGroupId"));
        dto.setLotMandatory(new Boolean(map.get("lotMandatory").toString()));
        dto.setLotType(map.get("lotType"));
        dto.setLotUnit(map.get("lotUnit"));
        if (map.get("lotThreshold") != null) {
            Object b = map.get("lotThreshold");
            dto.setLotThreshold(((Integer) b).intValue());
        }
        dto.setSerialRecordType(map.get("serialRecordType"));
        if (map.get("serialRecordLength") != null) {
            Object c = map.get("serialRecordLength");
            dto.setSerialRecordLength(((Integer) c).intValue());
        }
        dto.setHandlingUnitId(map.get("handlingUnitId"));
        dto.setMeasured(new Boolean(map.get("measured").toString()));
        if (map.get("height") != null && !"".equals(map.get("height"))) {
            dto.setHeight(new BigDecimal(map.get("height").toString()));
        }
        if (map.get("width") != null && !"".equals(map.get("width"))) {
            dto.setWidth(new BigDecimal(map.get("width").toString()));
        }
        if (map.get("depth") != null && !"".equals(map.get("depth"))) {
            dto.setDepth(new BigDecimal(map.get("depth").toString()));
        }
        dto.setSize(map.get("size"));
        if (map.get("weight") != null && !"".equals(map.get("weight"))) {
            dto.setWeight(new BigDecimal(map.get("weight").toString()));
        }
        if (map.get("volume") != null) {
            Object v = map.get("volume");
           // dto.setVolume(new BigDecimal(((Integer) v).longValue()));
            dto.setVolume(new BigDecimal(Long.valueOf(String.valueOf(v)).longValue()));
        }
        dto.setMultiplePart(new Boolean(map.get("multiplePart")));
        if (map.get("multiplePartAmount") != null) {
            Object d = map.get("multiplePartAmount");
            dto.setMultiplePartAmount(((Integer) d).intValue());
        }
        dto.setPreferOwnBox(new Boolean(map.get("preferOwnBox").toString()));
        dto.setPreferBag(new Boolean(map.get("preferBag").toString()));
        dto.setUseBubbleFilm(new Boolean(map.get("useBubbleFilm").toString()));
        ItemDataGlobal entity = itemDataGlobalMapper.toEntity(dto);
        boolean useFlag = true;
        //生成商品唯一编码
        while (useFlag) {
            String itemNo = RandomUtil.getItemNo();
            ItemDataGlobal global = itemDataGlobalRepository.getByItemDataNo(itemNo);
            if (global == null) {
                useFlag = false;
                entity.setItemNo(itemNo);
            }
        }
        return itemDataGlobalRepository.save(entity);
    }

    public Message updateSkuGlobal(MultipartFile file, Map<String, String> map, Message message){
        ItemDataGlobalDTO dto = mapToDTO(map);
        ItemDataGlobal entity = itemDataGlobalRepository.retrieve(dto.getId());
        itemDataGlobalMapper.updateEntityFromDTO(dto, entity);
//        List<ItemData> itemDataList = itemDataRepository.getByItemDataGlobal(entity.getId());
//        if (itemDataList != null && !itemDataList.isEmpty()) {
//            for (ItemData itemData : itemDataList) {
//                itemDataGlobalMapper.updateItemDataFromItemDataGlobal(itemData, entity);
//            }
//            itemDataRepository.save(itemDataList);
//        }
        int result = 1;
        long size=file.getSize();
        String fileName = file.getOriginalFilename();
        String contentType = FilenameUtils.getExtension(fileName);
        Matcher matcher = imgPattern.matcher(contentType);
        if(size>3*1024*1024) {
            result = 0;
            message.setMessage("文件大于3MB");
            message.setResult(result);
            return message;
        }
        if (!matcher.matches()) {
            result = 0;
            message.setMessage("不支持文件类型" + contentType);
            message.setResult(result);
            log.info("文件类型不支持");
            return message;
        }
        //上传路径
        //String path = "D:" + File.separator + "image" + File.separator;
        String saveFile = picPath + entity.getId() + "." + contentType;
        File uploadFile = new File(saveFile);
        //判断是否存在目录
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }
        try {
            FileUtils.touch(uploadFile);
            FileOutputStream out = new FileOutputStream(saveFile);
            BufferedOutputStream bs = new BufferedOutputStream(out);
            FileCopyUtils.copy(file.getInputStream(), bs);
        } catch (IOException e) {
            log.info("文件上传失败");
            throw new ApiException("文件上传失败");
        }
        entity.setPictureName(entity.getId() + "." + contentType);
        entity.setItemPicture(saveFile);
        itemDataGlobalRepository.save(entity);
        List<ItemData> itemDataList = itemDataRepository.getByItemDataGlobal(entity.getId());
        if (itemDataList != null && !itemDataList.isEmpty()) {
            for (ItemData itemData : itemDataList) {
                itemDataGlobalMapper.updateItemDataFromItemDataGlobal(itemData, entity);
            }
            itemDataRepository.save(itemDataList);
        }
        message.setResult(result);
        return message;
    }

    public ItemDataGlobalDTO mapToDTO(Map<String, String> map){
        ItemDataGlobalDTO dto=new ItemDataGlobalDTO();
        dto.setId(map.get("id"));
        dto.setSkuNo(map.get("skuNo"));
        dto.setName(map.get("name"));
        dto.setDescription(map.get("description"));
        Object safe = map.get("safetyStock");
        dto.setSafetyStock(((Integer) safe).intValue());
        dto.setItemGroupId(map.get("itemGroupId"));
        dto.setLotMandatory(new Boolean(map.get("lotMandatory").toString()));
        dto.setLotType(map.get("lotType"));
        dto.setLotUnit(map.get("lotUnit"));
        if (map.get("lotThreshold") != null) {
            Object lotThreshold = map.get("lotThreshold");
            dto.setLotThreshold(((Integer) lotThreshold).intValue());
        }
        dto.setSerialRecordType(map.get("serialRecordType"));
        if (map.get("serialRecordLength") != null) {
            Object length = map.get("serialRecordLength");
            dto.setSerialRecordLength(((Integer) length).intValue());
        }
        dto.setHandlingUnitId(map.get("handlingUnitId"));
        dto.setMeasured(new Boolean(map.get("measured").toString()));
        if (map.get("height") != null) {
            Object height = map.get("height");
            if(!height.equals(""))
                dto.setHeight(new BigDecimal(height.toString()));
        }
        if (map.get("width") != null) {
            Object width = map.get("width");
            if(!width.equals(""))
                dto.setWidth(new BigDecimal(width.toString()));
        }
        if (map.get("depth") != null) {
            Object depth = map.get("depth");
            if(!depth.equals(""))
                dto.setDepth(new BigDecimal(depth.toString()));
        }
        dto.setSize(map.get("size"));
        if (map.get("weight") != null) {
            Object weight = map.get("weight");
            if(!weight.equals(""))
                dto.setWeight(new BigDecimal(weight.toString()));
        }
        if (map.get("volume") != null) {
            Object volume = map.get("volume");
            dto.setVolume(new BigDecimal(Long.valueOf(String.valueOf(volume)).longValue()));
        }
        dto.setMultiplePart(new Boolean(map.get("multiplePart")));
        if (map.get("multiplePartAmount") != null) {
            Object amount = map.get("multiplePartAmount");
            dto.setMultiplePartAmount(((Integer) amount).intValue());
        }
        dto.setPreferOwnBox(new Boolean(map.get("preferOwnBox").toString()));
        dto.setPreferBag(new Boolean(map.get("preferBag").toString()));
        dto.setUseBubbleFilm(new Boolean(map.get("useBubbleFilm").toString()));
        return dto;
    }
}
