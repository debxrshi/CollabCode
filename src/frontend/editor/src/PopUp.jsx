import React, { useRef, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';




function PopUp({ onClose, textValue ,type}) {
  const [borderColor, setBorderColor] = useState('border-blue-500');
  const [fontColor, setFontColor] = useState();
  const inputRef = useRef();
  const [value, setValue] = useState('');
  const navigate = useNavigate();

  const handleError = async (e) => {
    e.preventDefault(); 
    const roomId = textValue; 
    const secretKey = inputRef.current.value;


    if (!roomId || !secretKey || roomId.trim().length === 0 || secretKey.trim().length === 0) {
      setBorderColor('border-red-500');
      setFontColor('placeholder-red-600');
      setTimeout(() => {
        setBorderColor('border-blue-500');
        setFontColor();
      }, 2000);
      return;
    }

    try {
      const response = await fetch('http://192.168.1.4:8080/api/createRoom', {
        method: 'POST', 
        headers: { 'Content-Type': 'application/json' }, 
        body: JSON.stringify({ uuid:roomId, roomKey:secretKey, code:"", lang:"python" }),
      });

      if (response.ok) {
        const data = await response.text(); 

        console.log('Room joined:', data); 
        localStorage.setItem("key",secretKey)
        navigate(`/CodeEditor/${textValue}`)
      } else {
        console.log('Failed to join room:', response.statusText);
        setBorderColor('border-red-500');
        setFontColor('placeholder-red-600');
      }
    } catch (error) {
      console.log('Error joining room:', error);
      setBorderColor('border-red-500');
      setFontColor('placeholder-red-600');
    }
  };
  const handleErrorJoin = async (e) => {
    e.preventDefault(); 
    const roomId = textValue; 
    const secretKey = inputRef.current.value;

    if (!roomId || !secretKey || roomId.trim().length === 0 || secretKey.trim().length === 0) {
      setBorderColor('border-red-500');
      setFontColor('placeholder-red-600');
      setTimeout(() => {
        setBorderColor('border-blue-500');
        setFontColor();
      }, 2000);
      return;
    }

    try {
      const response = await fetch(`http://192.168.1.4:8080/api/joinRoom?uuid=${textValue}&roomKey=${value}`, {
        method: 'Get', 
        headers: { 'Content-Type': 'application/json' }, 
        //body: JSON.stringify({ uuid:roomId, roomKey:secretKey, code:"", lang:"python" }),
      });

      if (response.ok) {
        const data = await response.json()

        console.log('Room joined:', data); 
        localStorage.setItem("key",secretKey)
        navigate(`/CodeEditor/${textValue}`)
      } else {
        console.log('Failed to join room:', response.statusText);
        setBorderColor('border-red-500');
        setFontColor('placeholder-red-600');
      }
    } catch (error) {
      console.log('Error joining room:', error);
      setBorderColor('border-red-500');
      setFontColor('placeholder-red-600');
    }
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50 bg-gray-700 bg-opacity-50">
      <div className="bg-white px-10 rounded-lg shadow-md h-60 flex flex-col justify-center">
        <div className="flex justify-between">
          <h2 className="mb-4 text-blue-600 font-bold py-2 text-xl mt-4 rounded focus:outline-none focus:shadow-outline w-fit">Room ID : {textValue} </h2>
          <button onClick={onClose} className="w-fit h-fit"><FontAwesomeIcon icon={faXmark} className="text-2xl text-slate-400 hover:text-red-600 relative bottom-[10px]" /></button>
        </div>
        <div>
          <input
            type="text"
            placeholder="Enter Room Key"
            className={`border py-2 px-24 pl-2 mt-4 text-lg mr-5 rounded-lg ${borderColor} ${fontColor}`}
            ref={inputRef}
            value={value}
            onChange={(e) => setValue(e.target.value)}
          />
          {type=='new' &&<button
            className="bg-white text-grey-300 hover:bg-green-600 border hover:text-white border-grey-300 py-2 px-4 mt-4 text-lg mr-5 rounded-lg font-bold py-2 px-4 mt-4 mb-4 text-lg rounded focus:outline-none focus:shadow-outline"
            onClick={handleError}
          >
            Create
          </button>}
          {type=='existing' &&<button
            className="bg-white text-grey-300 hover:bg-green-600 border hover:text-white border-grey-300 py-2 px-4 mt-4 text-lg mr-5 rounded-lg font-bold py-2 px-4 mt-4 mb-4 text-lg rounded focus:outline-none focus:shadow-outline"
            onClick={handleErrorJoin}
          >
            Join
          </button>}
        </div>
      </div>
    </div>
  );
}

export default PopUp;