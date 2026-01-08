<?php
// api/mark_done.php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

$response = ['success' => false, 'message' => ''];

$work_id = $_GET['work_id'] ?? $_POST['work_id'] ?? '';
$remark = $_GET['remark'] ?? $_POST['remark'] ?? '';

if (empty($work_id)) {
    $response['message'] = 'Work ID required';
    echo json_encode($response);
    exit();
}

// Simulate success
$response['success'] = true;
$response['message'] = "Work $work_id completed successfully";
$response['remark'] = $remark;
$response['completed_at'] = date('d-M-Y H:i:s');

echo json_encode($response, JSON_PRETTY_PRINT);
?>