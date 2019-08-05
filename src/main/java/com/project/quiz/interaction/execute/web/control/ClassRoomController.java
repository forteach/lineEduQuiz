package com.project.quiz.interaction.execute.web.control;


/**
 * @Description:创建课堂，加入学生
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/12/21  17:50
 * //
 */
//@RestController
//@Api(value = "课堂", tags = {"课堂,上课相关处理"})
//@RequestMapping(path = "/classRoom", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//public class ClassRoomController{
//
////    private final ClassRoomService classRoomService;
//
//    private final TokenService tokenService;
//
//    public ClassRoomController(
////            ClassRoomService classRoomService,
//                               TokenService tokenService) {
////        this.classRoomService = classRoomService;
//        this.tokenService = tokenService;
//    }

//    @ApiOperation(value = "老师有效期内创建同一临时课堂", notes = "有效期为2个小时 此方法两个小时内返回同一数据")
//    @PostMapping(value = "/create/reuse")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "(可以为“”，不能不传)已存在临时课堂ID", name = "circleId", dataType = "string", paramType = "from"),
//            @ApiImplicitParam(value = "教室ID", name = "teacherId", dataType = "string", paramType = "from"),
//            @ApiImplicitParam(value = "章节id", name = "chapterId", required = true, dataType = "string", paramType = "from")
//    })
//    public Mono<WebResult> createInteractiveRoom(@RequestBody InteractiveRoomVo roomVo, ServerHttpRequest request) {
//        MyAssert.blank(roomVo.getChapterId(), DefineCode.ERR0010 ,"章节编号不能为空");
//        Optional<String> teacherId = tokenService.getTeacherId(request);
//        teacherId.ifPresent(roomVo::setTeacherId);
//        MyAssert.blank(teacherId.get(), DefineCode.ERR0010 ,"教室编号不能为空");
//        return classRoomService.createInteractiveRoom(roomVo.getCircleId(),roomVo.getTeacherId(),roomVo.getChapterId()).map(WebResult::okResult);
//    }

//    @ApiOperation(value = "学生加入互动课堂", notes = "学生加入互动课堂")
//    @PostMapping(value = "/join/interactiveRoom")
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "课堂Id", name = "circleId", required = true, dataType = "string", paramType = "from")
//    })
//    public Mono<WebResult> joinInteractiveRoom(@ApiParam(value = "学生加入互动课堂", required = true) @RequestBody JoinInteractiveRoomVo joinVo, ServerHttpRequest request) {
////        //验证请求参数
//        MyAssert.blank(joinVo.getCircleId(), DefineCode.ERR0010 ,"课堂编号不存在");
//        joinVo.setExamineeId(tokenService.getStudentId(request));
//        return classRoomService.joinInteractiveRoom(joinVo.getCircleId(),joinVo.getExamineeId()).map(WebResult::okResult);
//    }

//    @ApiOperation(value = "查找加入过的学生", notes = "查找加入过的学生")
//    @PostMapping(value = "/find/interactiveStudents")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "课堂圈子id", name = "circleId", dataType = "string", paramType = "query", required = true),
//            @ApiImplicitParam(value = "教师id", name = "teacherId", required = true, dataType = "string", paramType = "from")
//    })
//    public Mono<WebResult> findInteractiveStudents(@ApiParam(value = "查找加入课堂的学生", required = true) @RequestBody InteractiveStudentsReq interactiveReq) {
//        MyAssert.blank(interactiveReq.getCircleId(), DefineCode.ERR0010, "课堂圈子id不能为空");
//        return classRoomService.findInteractiveStudents(interactiveReq.getCircleId(),interactiveReq.getTeacherId()).map(WebResult::okResult);
//    }

//}
