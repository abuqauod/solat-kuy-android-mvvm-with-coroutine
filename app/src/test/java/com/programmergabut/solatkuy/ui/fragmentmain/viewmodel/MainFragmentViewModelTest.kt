package com.programmergabut.solatkuy.ui.fragmentmain.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.programmergabut.solatkuy.CoroutinesTestRule
import com.programmergabut.solatkuy.DummyArgument
import com.programmergabut.solatkuy.data.PrayerRepositoryImpl
import com.programmergabut.solatkuy.data.local.localentity.NotifiedPrayer
import com.programmergabut.solatkuy.ui.activitymain.fragmentmain.FragmentMainViewModel
import com.programmergabut.solatkuy.util.Resource
import com.programmergabut.solatkuy.DummyRetValue
import com.programmergabut.solatkuy.data.QuranRepositoryImpl
import com.programmergabut.solatkuy.data.local.dao.NotifiedPrayerDao
import com.programmergabut.solatkuy.data.local.localentity.MsApi1
import com.programmergabut.solatkuy.data.local.localentity.MsSetting
import com.programmergabut.solatkuy.data.remote.RemoteDataSourceAladhan
import com.programmergabut.solatkuy.data.remote.remoteentity.readsurahJsonEn.ReadSurahEnResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainFragmentViewModelTest {

    private lateinit var viewModel: FragmentMainViewModel

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule: CoroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var prayerRepositoryImpl: PrayerRepositoryImpl

    @Mock
    private lateinit var quranRepositoryImpl: QuranRepositoryImpl

    private val msApi1 = DummyArgument.msApi1
    private val surahID = DummyArgument.surahID
    private val mapPrayer = DummyArgument.getMapPrayer()


    @Before
    fun before(){
        viewModel = FragmentMainViewModel(prayerRepositoryImpl, quranRepositoryImpl)

        verify(prayerRepositoryImpl).getMsApi1()
        verify(quranRepositoryImpl).getListFavAyah()
    }

    @Test
    fun syncNotifiedPrayer() = coroutinesTestRule.testDispatcher.runBlockingTest{
        //given
        val observer = mock<Observer<Resource<List<NotifiedPrayer>>>>()
        val dummyNotifiedPrayer = Resource.success(DummyRetValue.getNotifiedPrayer())

        `when`(prayerRepositoryImpl.syncNotifiedPrayer(msApi1)).thenReturn(dummyNotifiedPrayer.data)

        //when
        viewModel.syncNotifiedPrayer(msApi1)
        val result = viewModel.notifiedPrayer.value

        //--return value
        verify(prayerRepositoryImpl).syncNotifiedPrayer(msApi1)
        assertEquals(dummyNotifiedPrayer, result)

        //--observer
        viewModel.notifiedPrayer.observeForever(observer)
        verify(observer).onChanged(dummyNotifiedPrayer)
    }

    @Test
    fun fetchReadSurahEn() = coroutinesTestRule.testDispatcher.runBlockingTest{
        //given
        val observer = mock<Observer<Resource<ReadSurahEnResponse>>>()
        val dummyQuranSurah = Resource.success(DummyRetValue.surahEnID_1())
        `when`(quranRepositoryImpl.fetchReadSurahEn(surahID).await()).thenReturn(dummyQuranSurah.data)

        //when
        viewModel.fetchReadSurahEn(surahID)
        val result = viewModel.readSurahEn.value

        //--return value
        verify(quranRepositoryImpl).fetchReadSurahEn(surahID)
        assertEquals(dummyQuranSurah, result)

        //--observer
        viewModel.readSurahEn.observeForever(observer)
        verify(observer).onChanged(dummyQuranSurah)
    }

    @Test
    fun getMsSetting() = coroutinesTestRule.testDispatcher.runBlockingTest {

        //given
        val observer = mock<Observer<Resource<MsSetting>>>()
        val dummyLiveData: MutableLiveData<Resource<MsSetting>> = MutableLiveData()
        dummyLiveData.value = Resource.success(DummyRetValue.getMsSetting())

        //scenario
        `when`(prayerRepositoryImpl.getMsSetting()).thenReturn(dummyLiveData)

        //start observer
        viewModel.msSetting.observeForever(observer)

        //when
        viewModel.getMsSetting()
        val result = viewModel.msSetting.value

        //--verify
        verify(prayerRepositoryImpl).getMsSetting()
        assertEquals(dummyLiveData.value, result)
        verify(observer).onChanged(dummyLiveData.value)

        //end observer
        viewModel.msSetting.removeObserver(observer)
    }


    @Test
    fun updateMsApi1() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.updateMsApi1(msApi1)
        com.nhaarman.mockitokotlin2.verify(prayerRepositoryImpl).updateMsApi1(msApi1)
    }

    @Test
    fun updatePrayerIsNotified() = coroutinesTestRule.testDispatcher.runBlockingTest {

        viewModel.updatePrayerIsNotified(mapPrayer.keys.elementAt(0), true)
        com.nhaarman.mockitokotlin2.verify(prayerRepositoryImpl).updatePrayerIsNotified(mapPrayer.keys.elementAt(0), true)
    }

    @Test
    fun updateIsUsingDBQuotes() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.updateIsUsingDBQuotes(true)
        com.nhaarman.mockitokotlin2.verify(prayerRepositoryImpl).updateIsUsingDBQuotes(true)
    }

}