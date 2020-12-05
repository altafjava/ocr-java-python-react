import React, { useState } from 'react';
import ConversionService from '../api/ConversionService';
import { DANGER, INFO, SUCCESS, WARNING } from '../constant/Color';
import AlertMessage from './AlertMessage';
import './Conversion.css';

const Conversion = () => {
  const [file, setFile] = useState(null);
  const [isButtonDisabled, setButtonDisabled] = useState(false);
  const [downloadUrl, setDownloadUrl] = useState('');
  const [result, setResult] = useState('');
  const [image, setImage] = useState(null);
  const [notification, setNotification] = useState({});

  const nameStartEndLength = 12;
  const fileTypes = [
    { key: 'png', value: 'PNG' },
    { key: 'jpg', value: 'JPG' },
    { key: 'jpeg', value: 'JPEG' },
    { key: 'gif', value: 'GIF' },
  ];

  const uploadClick = () => {
    var inputTypeFile = document.getElementById('inputTypeFile');
    inputTypeFile.click();
  };

  const handleFileChange = (e) => {
    const btn = document.getElementById('convert-button');
    btn.innerHTML = "Convert <i id='spinner' class='fa spin'></i>";
    enableButton(btn);
    const uploadedFile = e.target.files[0];
    setFile(uploadedFile);
    setImage(URL.createObjectURL(uploadedFile));
    if (uploadedFile === undefined) {
      setButtonDisabled(true);
    } else {
      const regex = /(?:\.([^.]+))?$/;
      let fileExtension = regex.exec(uploadedFile.name)[1];
      if (fileExtension === undefined) {
        showAlert('File has no extension. Please choose different file', WARNING);
        setButtonDisabled(true);
      } else {
        const isFileTypePresent = fileTypes.filter((fileType) => fileType.key === fileExtension.toLowerCase()).length > 0;
        if (!isFileTypePresent) {
          showAlert('Sorry, Currently we are not converting .' + fileExtension + ' file', INFO);
          setButtonDisabled(true);
        } else {
          setButtonDisabled(false);
        }
      }
    }
  };

  const handleConvert = (e) => {
    let btn = document.getElementById('convert-button');
    if (btn.innerText.trim() === 'Convert') {
      e.preventDefault();
      if (file.name === undefined) {
        showAlert('Please choose a file', WARNING);
      } else {
        btn.innerHTML = "Converting <i id='spinner' class='fa fa-spinner spin'></i>";
        disableButton(btn);
        ConversionService.convert(file)
          .then((response) => {
            setResult(response.data.content);
            showAlert('File successfully converted. Click the Download button to download', SUCCESS);
            setDownloadUrl(response.data.downloadUrl);
            btn.innerHTML = "Download <i id='spinner' class='fa spin'></i>";
            enableButton(btn);
          })
          .catch((error) => {
            if (error.response) {
              showAlert(error.response.data, DANGER);
            } else {
              showAlert(error.message, DANGER);
            }
            btn.innerHTML = "Convert <i id='spinner' class='fa spin'></i>";
          });
      }
    } else if (btn.innerText.trim() === 'Download') {
    } else {
      e.preventDefault();
      showAlert("Don't try to be oversmart", INFO);
    }
  };
  const disableButton = (btn) => {
    btn.classList.add('button__disabled');
    btn.classList.remove('button__hover');
  };
  const enableButton = (btn) => {
    btn.classList.remove('button__disabled');
    btn.classList.add('button__hover');
  };
  const showAlert = (message, background) => {
    setNotification({ hasError: true, message: message, background: background });
    setTimeout(() => setNotification({ hasError: false, message: message }), 3000);
  };

  return (
    <div className='main__container'>
      {notification.hasError && <AlertMessage background={notification.background} message={notification.message} />}
      <div className='container'>
        <div className='hide'>
          <input id='inputTypeFile' type='file' onChange={handleFileChange} />
        </div>
        <div className='div__upload' onClick={uploadClick}>
          <img src='/assets/icons/upload-icon.svg' alt='upload files' width='50px' />
          <div>Upload Image</div>
        </div>
        {file && (
          <div className='upload__details'>
            <table>
              <tbody>
                <tr>
                  <td className='align-right'>Name :</td>
                  <td>
                    {file && file.name.length > nameStartEndLength + nameStartEndLength + 8
                      ? file.name.substring(0, nameStartEndLength) +
                        '....' +
                        file.name.substring(file.name.length - nameStartEndLength, file.name.length)
                      : file.name}
                  </td>
                </tr>
                <tr>
                  <td className='align-right'>Size :</td>
                  {file && <td>{`${file.size / 1000} KB`}</td>}
                </tr>
                <tr>
                  <td className='align-right'>Type :</td>
                  <td>{file && file.type}</td>
                </tr>
              </tbody>
            </table>
          </div>
        )}
        <div className='div__button'>
          <a
            id='convert-button'
            className={`button button__hover ${isButtonDisabled ? ' button__disabled' : ''}`}
            href={downloadUrl}
            onClick={handleConvert}
          >
            Convert <i id='spinner' className='fa spin'></i>
          </a>
        </div>
      </div>
      <div className='preview__container'>
        <div className={`div__image-preview`}>
          <div className='result__heading'>Image Preview</div>
          <div>
            <img src={image} alt='preview' width='100%'></img>
          </div>
        </div>
        <div className='div__result'>
          <div className='result__heading'>Text Found on Your Image is</div>
          <div style={{ padding: `${result === '' ? '16px' : '32px'}` }}>
            {result.split('\n').map((s, index) => (
              <p key={index} className='result'>
                {s}
              </p>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Conversion;
