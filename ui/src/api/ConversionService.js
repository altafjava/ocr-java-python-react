import Axios from 'axios';

class ConversionService {
  convert(file) {
    const formData = new FormData();
    formData.append('file', file);
    return Axios({
      method: 'POST',
      url: 'http://localhost:8080/convert',
      data: formData,
      headers: { 'Access-Control-Allow-Origin': '*', 'Content-Type': 'multipart/form-data' },
    });
  }
}
export default new ConversionService();
