package com.example.demo.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.HTTPException;
import com.example.demo.dataTransfer.EventDataTransfer;
import com.example.demo.dataTransfer.UserDataTransfer;
import com.example.demo.repository.EventRepository;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMessages;

@Service
public class EventService {
Logger logger = LoggerFactory.getLogger(EventService.class);
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	EntityManagerFactory emf;
	
	public void PagesService(EntityManagerFactory emf)
	{
		logger.info("Adding bean of entity managaer factory for test cases purpose");
		this.emf = emf;
	}

	/**
	 * @param nVal
	 * @param selectedDate
	 * @param startDate
	 * @param endDate
	 * accepts nVal value and gets the n most viewed pages from DB
	 * accepts selectedDate value and gets n most viewed pages based on selected date
	 * accepts startDate & endDate and gets n most viewed pages within the time range
	 * @return list of n most viewed pages
	 * @throws custom exception if nVal is negative value / provided dates are invalid
	 */
	public List<EventDataTransfer> getMostViewedPages(int nVal,String selectedDate,String startDate,String endDate)
	{
		EntityManager em = emf.createEntityManager();
		Query query = null;
		List<EventDataTransfer> result = null;
		try
		{
			if (nVal <= 0) {
				logger.error("nVal is invalid for the most viewed pages report");
				throw new HTTPException(ErrorMessages.N_VALUE_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			
			StringBuilder queryBuilder = new StringBuilder("");
			if(startDate!=null && !startDate.equals("null") && endDate!=null && !endDate.equals("null"))
			{
				DateVerification.verifyDateFormat(startDate);
				DateVerification.verifyDateFormat(endDate);
				
				queryBuilder.append("and date(pg.dateValue) between date('"+startDate+"') and date('"+endDate+"')");
			}
			else if(selectedDate!=null && !selectedDate.equals("null"))
			{
				DateVerification.verifyDateFormat(selectedDate);
				queryBuilder.append("and date(pg.dateValue)=date('"+selectedDate+"')");
			}
			query = em.createQuery("select new com.example.demo.dto.EventDataTransfer(pg.eventLabel,count(pg.eventLabel)) "
					+ "from com.example.demo.entity.Pages as pg where pg.eventAction=:filter "+queryBuilder.toString()+" group by pg.eventLabel order by count(pg.eventLabel) desc");
			query.setMaxResults(nVal);
			query.setParameter("filter", "view");
			
			result = query.getResultList();
		}
		catch(HTTPException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			logger.error("Error occured in getting most viewd pages, error details : {}",e.getMessage());
		}
		finally
		{
			em.close();
		}
		return result;
	}
	
	/**
	 * @param nVal
	 * @param selectedDate
	 * @param startDate
	 * @param endDate
	 * accepts nVal value and gets the n most time spent pages from DB
	 * accepts selectedDate value and gets n most time spent pages based on selected date
	 * accepts startDate & endDate and gets n most time spent pages within the time range
	 * @return list of n most time spent pages
	 * @throws custom exception if nUsers is negative value / provided dates are invalid
	 */
	public List<EventDataTransfer> getMostTimeSpentPages(int nVal,String selectedDate,String startDate,String endDate)
	{
		EntityManager em = emf.createEntityManager();
		Query query = null;
		List<EventDataTransfer> result = null;
		try
		{
			
			if (nVal <= 0) {
				logger.error("nVal is invalid for the most time spent pages report");
				throw new HTTPException(ErrorMessages.N_VALUE_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			
			StringBuilder queryBuilder = new StringBuilder("");
			if(startDate!=null && !startDate.equals("null") && endDate!=null && !endDate.equals("null"))
			{
				DateVerification.verifyDateFormat(startDate);
				DateVerification.verifyDateFormat(endDate);
				
				queryBuilder.append(" and date(pg.dateValue) between date('"+startDate+"') and date('"+endDate+"')");
			}
			else if(selectedDate!=null && !selectedDate.equals("null"))
			{
				DateVerification.verifyDateFormat(selectedDate);
				queryBuilder.append(" and date(pg.dateValue)=date('"+selectedDate+"')");
			}
			query = em.createQuery("select new com.example.demo.dto.EventDataTransfer(pg.eventLabel,sum(pg.eventValue)) "
					+ "from com.example.demo.entity.Pages as pg where pg.id>0 "+queryBuilder.toString()+" group by pg.eventLabel order by sum(pg.eventValue) desc");
			query.setMaxResults(nVal);
			
			result = query.getResultList();
		}
		catch(HTTPException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			logger.error("Error occured in getting most time spent pages report, Error details : {}",e.getMessage());
		}
		finally
		{
			em.close();
		}
		return result;
	}
	
	/**
	 * @param nUsers
	 * @param selectedDate
	 * @param startDate
	 * @param endDate
	 * accepts nUsers value and gets the n top time spent users from DB 
	 * accepts selectedDate value and gets the top time spent users at that day
	 * accepts startDate & endDate  and gets the top time spent users in these date range
	 * @return list of n users who spent more time
	 * @throws custom exception if nUsers is negative value / provided dates are invalid
	 */
	public List<UserDataTransfer> getMostTimeSpentUser(int nUsers, String selectedDate,String startDate,String endDate)
	{
		EntityManager em = emf.createEntityManager();
		Query query = null;
		List<UserDataTransfer> result = null;
		try
		{
			
			if (nUsers <= 0) {
				logger.error("nVal is invalid for the most time spent users report");
				throw new HTTPException(ErrorMessages.N_VALUE_VALIDATION, HttpStatus.BAD_REQUEST);
			}
			
			StringBuilder queryBuilder = new StringBuilder("");
			if(startDate!=null && !startDate.equals("null") && endDate!=null && !endDate.equals("null"))
			{
				DateVerification.verifyDateFormat(startDate);
				DateVerification.verifyDateFormat(endDate);
				
				queryBuilder.append(" and date(pg.dateValue) between date('"+startDate+"') and date('"+endDate+"')");
			}
			else if(selectedDate!=null && !selectedDate.equals("null"))
			{
				DateVerification.verifyDateFormat(selectedDate);
				queryBuilder.append(" and date(pg.dateValue)=date('"+selectedDate+"')");
			}
			query = em.createQuery("select new com.example.demo.dto.UserDataTransfer(pg.uuid,sum(pg.eventValue)) "
					+ "from com.example.demo.entity.Pages as pg where pg.id>0 "+queryBuilder.toString()+" group by pg.uuid order by sum(pg.eventValue) desc");
			query.setMaxResults(nUsers);
			
			result = query.getResultList();
		}
		catch(HTTPException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			logger.error("Error occured in getting most time spent report. error description {}",e.getMessage());	
		}
		finally
		{
			em.close();
		}
		return result;

}
